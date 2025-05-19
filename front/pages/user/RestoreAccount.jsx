import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../../style/user/RestoreAccount.css';
import { authAPI, userAPI } from "../../api/api.js";
import axios from 'axios';

const RestoreAccount = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [email] = useState(location.state?.email || '');
    const [verificationCode, setVerificationCode] = useState('');
    const [step, setStep] = useState(1); // 1: 이메일 입력, 2: 인증 코드 입력
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSendVerificationCode = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await authAPI.sendVerificationCode(email);
            setStep(2);
        } catch (error) {
            console.error('메일 인증 에러:', error);
            setError(error.response?.data?.message || '인증 코드 전송에 실패했습니다.');
        } finally {
            setLoading(false);
        }
    };

    const handleVerifyAndRestore = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const verifyResponse = await authAPI.verifyCode(email, verificationCode);

            if (verifyResponse.status === 200) {
                // 계정 복구
                const restoreResponse = await userAPI.restoreAccount(email);
                console.log(restoreResponse);

                if (restoreResponse.status === 200) {
                    alert(restoreResponse.data.message);
                    navigate('/mypage');
                }
            }
        } catch (error) {
            console.error('Error:', error);
            if (error.response) {
                setError(error.response.data.error || error.response.data.message || '계정 복구에 실패했습니다.');
            } else {
                setError('서버와의 통신 중 오류가 발생했습니다.');
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="restore-account-page">
            <div className="restore-form">
                <h3>계정 복구</h3>
                {step === 1 ? (
                    <form onSubmit={handleSendVerificationCode}>
                        <p>계정 복구를 위해 이메일 인증이 필요합니다.</p>
                        <div className="form-group">
                            <label>이메일</label>
                            <input
                                type="email"
                                value={email}
                                readOnly
                            />
                        </div>
                        {error && <p className="error-message">{error}</p>}
                        <button type="submit" disabled={loading}>
                            {loading ? '처리 중...' : '인증 코드 전송'}
                        </button>
                    </form>
                ) : (
                    <form onSubmit={handleVerifyAndRestore}>
                        <p>이메일로 전송된 인증 코드를 입력해주세요.</p>
                        <div className="form-group">
                            <label>인증 코드</label>
                            <input
                                type="text"
                                value={verificationCode}
                                onChange={(e) => setVerificationCode(e.target.value)}
                                required
                            />
                        </div>
                        {error && <p className="error-message">{error}</p>}
                        <button type="submit" disabled={loading}>
                            {loading ? '처리 중...' : '계정 복구하기'}
                        </button>
                        <button type="button" onClick={() => setStep(1)} className="back-button">
                            이전으로
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default RestoreAccount;