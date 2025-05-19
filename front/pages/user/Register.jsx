import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../style/user/Register.css";
import { authAPI } from "../../api/api.js";

function Register() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        uEmail: "",
        uPassword: "",
        confirmPassword: "",
        uName: "",
    });
    const [error, setError] = useState("");

    const handleChange = (e) => {
        const { name, value } = e.target;
        setForm({ ...form, [name]: value });
        if (name === "confirmPassword" || name === "uPassword") {
            setError("");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (form.uPassword !== form.confirmPassword) {
            setError("비밀번호가 일치하지 않습니다.");
            return;
        }
        try {
            // 1단계: 회원정보 임시 저장 및 인증코드 발송
            await authAPI.requestRegister({
                uEmail: form.uEmail,
                uPassword: form.uPassword,
                uName: form.uName
            });
            // 2단계: 이메일 인증 페이지로 이동
            navigate("/email-verification", { 
                state: { 
                    email: form.uEmail, 
                    uPassword: form.uPassword, 
                    uName: form.uName 
                } 
            });
        } catch (error) {
            console.error("회원가입 요청 실패:", error);
            if (error.response && error.response.data) {
                setError(error.response.data.error || error.response.data.message || "회원가입 요청 실패");
            } else {
                setError("회원가입 요청 중 오류가 발생했습니다.");
            }
        }
    };

    return (
        <div className="register-page">
            <h2 className="logo" onClick={() => navigate("/")}>STUDYLOG</h2>
            <form onSubmit={handleSubmit} className="register-form">
                <label htmlFor="uEmail">이메일</label>
                <input
                    type="email"
                    id="uEmail"
                    name="uEmail"
                    placeholder="이메일 입력"
                    value={form.uEmail}
                    onChange={handleChange}
                    required
                />

                <label htmlFor="uPassword">비밀번호</label>
                <input
                    type="password"
                    id="uPassword"
                    name="uPassword"
                    placeholder="비밀번호 입력"
                    value={form.uPassword}
                    onChange={handleChange}
                    required
                />

                <label htmlFor="confirmPassword">비밀번호 확인</label>
                <input
                    type="password"
                    id="confirmPassword"
                    name="confirmPassword"
                    placeholder="비밀번호 재입력"
                    value={form.confirmPassword}
                    onChange={handleChange}
                    required
                />

                <label htmlFor="uName">이름</label>
                <input
                    type="text"
                    id="uName"
                    name="uName"
                    placeholder="이름 입력"
                    value={form.uName}
                    onChange={handleChange}
                    required
                />
                {error && <p className="error-message">{error}</p>}
                <button type="submit">회원가입</button>
                <p onClick={() => navigate("/login") } className="login-link">
                    이미 계정이 있으신가요? <span>로그인</span>
                </p>
            </form>
        </div>
    );
}

export default Register;
