import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { userAPI, authAPI } from "../../api/api.js";
import { updateUserInfo } from '../../store/authSlice';
import "../../style/user/MyInfoSection.css";

const MyInfoSection = () => {
    const [user, setUser] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [newName, setNewName] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const dispatch = useDispatch();

    useEffect(() => {
        fetchUserProfile();
    }, []);

    const fetchUserProfile = async () => {
        try {
            const response = await userAPI.getProfile();
            const userData = response.data;
            setUser({
                name: userData.uName,
                email: userData.uEmail,
            });
            setNewName(userData.uName);
        } catch (error) {
            console.error('프로필 조회 실패:', error);
            setError('프로필 정보를 불러오는데 실패했습니다.');
        }
    };

    const handleEditClick = () => {
        setIsEditing(true);
    };

    const handleCancelClick = () => {
        setIsEditing(false);
        setNewName(user.name);
    };

    const handleSaveClick = async () => {
        try {
            await userAPI.updateName({ uName: newName });
            // 이름 변경 후 accessToken 재발급
            const refreshRes = await authAPI.refresh();
            const newAccessToken = refreshRes.data.accessToken;
            if (newAccessToken) {
                localStorage.setItem('accessToken', newAccessToken);
            }
            // 최신 사용자 정보로 userState 갱신
            const profileRes = await userAPI.getProfile();
            const userData = profileRes.data;
            dispatch(updateUserInfo({
                Name: userData.uName,
                Email: userData.uEmail,
                Role: userData.uRole,
                deletedAt: userData.deletedAt
            }));
            setUser(prev => ({ ...prev, name: newName }));
            setIsEditing(false);
            alert('이름이 성공적으로 변경되었습니다.');
        } catch (error) {
            console.error('이름 변경 실패:', error);
            setError('이름 변경에 실패했습니다.');
        }
    };

    if (!user) return <div>로딩 중...</div>;

    return (
        <div className="user-info-section">
            <h3>내 정보</h3>
            {error && <div className="error-message">{error}</div>}
            <div className="user-info">
                {isEditing ? (
                    <div className="edit-form">
                        <div className="form-group">
                            <label>이름:</label>
                            <input
                                type="text"
                                value={newName}
                                onChange={(e) => setNewName(e.target.value)}
                                className="name-input"
                            />
                        </div>
                        <div className="button-group">
                            <button onClick={handleSaveClick} className="save-button">저장</button>
                            <button onClick={handleCancelClick} className="cancel-button">취소</button>
                        </div>
                    </div>
                ) : (
                    <>
                <p><strong>이름:</strong> {user.name}</p>
                <p><strong>이메일:</strong> {user.email}</p>
                        <div className="button-group">
                            <button onClick={handleEditClick} className="edit-button">이름 수정</button>
            </div>
                    </>
                )}
            </div>
        </div>
    );
};

export default MyInfoSection; 