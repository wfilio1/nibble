import React, { useContext, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import AuthContext from "../../context/AuthContext";
import { Button } from "react-bootstrap";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState([]);

  const auth = useContext(AuthContext);

  const navigate = useNavigate();

  const isFocused = useIsFocused();

  useEffect(() => {
    console.log("Errors:", errors);
  }, [errors]);

  const handleSubmit = async (event) => {
    event.preventDefault();

    const response = await fetch("http://localhost:8080/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });
    
      // This code executes if the request is successful
      if (response.status === 200) {
        const { jwt_token } = await response.json();
        auth.login(jwt_token);
        navigate("/");
      // This code executes if the request fails
      } else if (response.status === 403) {
        setErrors(["Login failed, please ensure the username and password are correct."]);
        // console.log("Errors after setting state:", errors);
      } else {
        setErrors(["Unknown error."]);
        // console.log("Errors after setting state:", errors);
      } 

      
      console.log("Response status:", response.status);
      console.log(errors);

  };

  return (
    <div className="authentication">
      <h2 className="auth-title">Login</h2>
      <ul>
        {errors.map(error => <li key={error}>{error}</li>)}
      </ul>
      <form onSubmit={handleSubmit}>
        <div className="login-input-container">
          <label htmlFor="username" className="login-input-label">Username:</label>
          <input
            type="text"
            onChange={(event) => setUsername(event.target.value)}
            value={username}
            id="username"
            className="login-input-field"
          />
        </div>
        <div className="login-input-container">
          <label htmlFor="password" className="login-input-label">Password:</label>
          <input
            type="password"
            onChange={(event) => setPassword(event.target.value)}
            value={password}
            id="password"
            className="login-input-field"
          />
        </div>
        <div className="login-button-container">
          <button className="login-button" type="submit">Login</button>
          <Link to="/" >
            <button className="cancel-button" type="button">Cancel</button>
          </Link>
        </div>
        <div className="login-register-link">
          <Link to="/register" style={{ color: 'white' }}>Don't have an account yet?</Link>
        </div>
      </form>
    </div>
  );
}