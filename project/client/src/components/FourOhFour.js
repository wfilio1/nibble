import { Alert, Container, Nav } from "react-bootstrap";



const FourOhFour = () => {
  return (
    <Alert key="light" variant="light" className="err-center-content">
      <img src="/404.png" alt="pageNotFound" />
      <div className="err-content-container">
        <p className="err-alert-text">
          Oops, looks like you took a wrong turn somewhere! <br />
          But don't worry, we can help you out! <br />
          Choose a link below, or use our navigation bar to be properly directed.
        </p>
        <div className="err-alert-links">
          <Alert.Link href="/" style={{ color: 'white' }}>Home</Alert.Link>
          <Alert.Link href="/login" style={{ color: 'white' }}>Login</Alert.Link>
          <Alert.Link href="/register" style={{ color: 'white' }}>Register</Alert.Link>
          <Alert.Link href="/recipes" style={{ color: 'white' }}>Recipes</Alert.Link>
        </div>
      </div>
    </Alert>
  );
};


export default FourOhFour;