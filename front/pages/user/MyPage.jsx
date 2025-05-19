import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { updateUserInfo } from '../../store/authSlice';
import "../../style/user/MyPage.css";
import "../../style/user/modal.css";
import "../../style/user/deleteAccount.css";
import MyInfoSection from './MyInfoSection';
import DeleteAccountSection from './DeleteAccountSection';
import MyPostsSection from '../board/MyPostsSection';
import { userAPI } from "../../api/api.js";

const MyPage = () => {
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [user, setUser] = useState(null);
    const [selectedMenu, setSelectedMenu] = useState('info');
    const [error, setError] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if (!token) {
            alert("로그인이 필요합니다.");
            navigate("/login", { replace: true });
            return;
        }

        const fetchUserProfile = async () => {
            try {
                // 사용자 프로필 정보 조회
                const res = await userAPI.getProfile();
                const userData = res.data;
                
                dispatch(updateUserInfo({
                    Name: userData.uName,
                    Email: userData.uEmail,
                    Role: userData.uRole
                }));

                setUser({
                    name: userData.uName,
                    email: userData.uEmail,
                    role: userData.uRole,
                    deletedAt: userData.deletedAt ? new Date(userData.deletedAt).toLocaleString() : null
                });
            } catch (error) {
                console.error('사용자 정보 조회 실패:', error);
                if (error.response?.status === 401) {
                    alert("로그인이 만료되었습니다. 다시 로그인해주세요.");
                    navigate("/login");
                } else {
                    setError('사용자 정보를 불러오는데 실패했습니다.');
                }
            }
        };

        fetchUserProfile();
    }, [navigate, dispatch]);

    if (!user) return <div>로딩 중...</div>;

    return (
        <div className="mypage-wrapper">
            <nav className="sidebar">
                <h2>마이페이지</h2>
                <ul>
                    <li className={selectedMenu === 'info' ? 'active' : ''} onClick={() => setSelectedMenu('info')}>내 정보</li>
                    <li className={selectedMenu === 'post' ? 'active' : ''} onClick={() => setSelectedMenu('post')}>내 게시글</li>
                    <li className={selectedMenu === 'delete' ? 'active' : ''} onClick={() => setSelectedMenu('delete')}>회원 탈퇴</li>
                </ul>
            </nav>
            <main className="main-content">
                {error && <div className="error-message">{error}</div>}
                {selectedMenu === 'info' && <MyInfoSection />}
                {selectedMenu === 'post' && <MyPostsSection email={user.email} />}
                {selectedMenu === 'delete' && <DeleteAccountSection />}
            </main>
        </div>
    );
};

export default MyPage;
