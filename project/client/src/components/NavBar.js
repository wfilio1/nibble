import { useContext } from "react";
import { Button, Container, Nav, Navbar } from "react-bootstrap";
import AuthContext from "../context/AuthContext";

const NavBar = () => {

    const auth = useContext(AuthContext)
    console.log("auth.user:", auth.user)

    return (
      <Navbar bg="light" data-bs-theme="light">
        <Container className="d-flex flex-column align-items-center">
          <Navbar.Brand href="/" className="website-title mb-3">Nibble Neighborly</Navbar.Brand>
          <Nav className="mb-3 nav-links-container">
            <Nav.Link className="nav-link-white" href="/">Home</Nav.Link>
            <Nav.Link className="nav-link-white" href="/recipes">Recipes</Nav.Link>
            <Nav.Link className="nav-link-white" href={auth.user ? `/add-recipe` : `/login`}>Add A Recipe</Nav.Link>
            <Nav.Link className="nav-link-white" href={auth.user ? `/pantry` : '/login'}>
              My Pantry
            </Nav.Link>
            <Nav.Link className="nav-link-white" href="/music">LoFi while Cooking</Nav.Link>

            {auth.user === null ? (
              <>
                <Nav.Link className="nav-link-white" href="/login">Login</Nav.Link>
                <Nav.Link className="nav-link-white" href="/register">Register</Nav.Link>
              </>
            ) : null}
          </Nav>
          {auth.user && (
            <div className="welcome-message">
              Welcome back, {auth.user.username}!
              <span className="space"></span>
              <Button onClick={() => auth.logout()}>Logout</Button>
            </div>
          )}
        </Container>
      </Navbar>
    );
}

export default NavBar;