import { useState } from "react";
import { Button } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";


function RegistrationForm() {

  const [errors, setErrors] = useState([])

  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (evt) => {
    evt.preventDefault()

    const newUser = {
      username, password
    }

    let verb = "POST";
    let url = "http://localhost:8080/create_account"

    fetch(url, {
      method: verb,
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(newUser)
    })
    .then(response => {
      if (response.ok) {
        response.json()
        .then(q => {
          setUsername(q.username)
          setPassword(q.password)
          navigate(`/`)
        })
      } else {
        response.json()
        .then(errors => {
          if (Array.isArray(errors)) {
            setErrors(errors)
          } else {
            setErrors([errors])
          }
        })
      }
    })

  }



  return (
    <div className="authentication">
      <h2 className="auth-title">Register</h2>
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
          <button className="login-button" type="submit">Register</button>
          <Link to="/">
            <button className="cancel-button" type="button">Cancel</button>
          </Link>
        </div>
        <div className="login-register-link">
          <Link to="/login" style={{ color: 'white' }}>Already have an account?</Link>
        </div>
      </form>
    </div>
  );
}

export default RegistrationForm;