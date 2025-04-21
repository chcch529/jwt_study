import { Link } from "react-router-dom";

export const LoginPage = () => {
    return(
        <>
            <Link
                to={"http://localhost:8080/oauth2/authorization/google"}
            >
                Google Login
            </Link>
            <br />
            <Link
                to={"http://localhost:8080/oauth2/authorization/kakao"}
            >
                Kakao Login
            </Link>
            <br />  
            <Link
                to={"http://localhost:8080/oauth2/authorization/naver"}
            >
                Naver Login
            </Link>
            
        </>
    );
};