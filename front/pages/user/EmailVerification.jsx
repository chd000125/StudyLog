import { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
// import "../../style/user/EmailVerification.css";
import { authAPI } from "../../api/api.js";

function EmailVerification() {
    const navigate = useNavigate();
    const location = useLocation();
    const [verificationCode, setVerificationCode] = useState("");
    const [error, setError] = useState("");
    const { email, uPassword, uName } = location.state || {};

    if (!email) {
        navigate("/register");
        return null;
    }

    const handleVerifyCode = async (e) => {
        e.preventDefault();
        try {
            // 1. 인증코드 검증
            const response = await authAPI.verifyCode(email, verificationCode);
            if (response.data.message === "이메일 인증 성공") {
                // 2. 인증 성공 시 실제 회원가입 요청
                await authAPI.requestRegisterComplete({ uEmail: email, uPassword, uName });
                localStorage.removeItem('tempRegisterData');
                alert("회원가입이 성공적으로 완료되었습니다! 로그인 해주세요.");
                navigate("/login");
            } else {
                // 인증 실패 시(혹시라도 200 OK로 에러 메시지 반환되는 경우)
                setError(response.data.error || response.data.message || "인증 코드가 일치하지 않습니다.");
            }
        } catch (error) {
            // 인증 실패(401 등) 시
            if (error.response && error.response.data) {
                setError(error.response.data.error || error.response.data.message || "인증 코드가 일치하지 않습니다.");
            } else {
                setError("인증 코드 확인 중 오류가 발생했습니다.");
            }
        }
    };

    const handleResendCode = async () => {
        try {
            await authAPI.sendVerificationCode(email);
            setError("");
            alert("인증 코드가 재전송되었습니다.");
        } catch (error) {
            if (error.response && error.response.data) {
                setError(error.response.data.message || "인증 코드 재전송에 실패했습니다.");
            } else {
                setError("인증 코드 재전송 중 오류가 발생했습니다.");
            }
        }
    };

    return (
        <div className="verification-page">
            <h2 className="logo" onClick={() => navigate("/")}>STUDYLOG</h2>
            <div className="verification-form">
                <h3>이메일 인증</h3>
                <p className="email-info">{email}로 전송된 인증 코드를 입력해주세요.</p>
                <form onSubmit={handleVerifyCode}>
                    <div>
                        <label htmlFor="verificationCode">인증 코드</label>
                        <input
                            type="text"
                            id="verificationCode"
                            value={verificationCode}
                            onChange={(e) => setVerificationCode(e.target.value)}
                            required
                        />
                    </div>
                    {error && <p className="error-message">{error}</p>}
                    <button type="submit">인증하기</button>
                    <button type="button" onClick={handleResendCode}>인증 코드 재전송</button>
                    <button type="button" onClick={() => navigate("/register")}>이전으로</button>
                </form>
            </div>
        </div>
    );
}

export default EmailVerification; 