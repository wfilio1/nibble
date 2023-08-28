import { Button, Carousel } from "react-bootstrap";
import { Link } from "react-router-dom";
import AuthContext from "../context/AuthContext";
import { useContext } from "react";

const LandingPage = () => {
  const auth = useContext(AuthContext);


    return(

      <>
      <Carousel>
          <Carousel.Item interval={3000}>
            <img className="d-block w-100" src="https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80" alt="food" />
          </Carousel.Item>
          <Carousel.Item interval={3000}>
            <img className="d-block w-100" src="https://images.unsplash.com/photo-1490645935967-10de6ba17061?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2053&q=80" alt="food2" />
          </Carousel.Item>
          <Carousel.Item interval={3000}>
            <img className="d-block w-100" src="https://images.unsplash.com/photo-1496116218417-1a781b1c416c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80" alt="food3" />
          </Carousel.Item>
          <Carousel.Item interval={3000}>
            <img className="d-block w-100" src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80" alt="food4" />
          </Carousel.Item>
          <Carousel.Item interval={3000}>
            <img className="d-block w-100" src="https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80" alt="food5" />
          </Carousel.Item>
        </Carousel>


      <div className="website-mission">
        <p className="mission-text">
        At Nibble Neighborly, we believe in the power of community and passion. 
        Our mission is to address and alleviate the stress caused by rising living expenses faced by our community members – 
        our friends, family, loved ones, and neighbors. Through the shared passion for cooking, we unite as a community. 
        We tackle the issue of food waste head-on by advocating mindful meal planning and resourceful cooking techniques, 
        knowledge shared by our very own members. By clicking the link below, you'll witness some of the incredible dishes created 
        and generously shared by our members.
        </p>

        <div className="landingbutton-container">
          <Link to="/recipes">
            <Button variant="primary">Take a Look at Our Members' Creations!</Button>
          </Link>
          </div>

        <p className="mission-text">
        In our endeavor, we strive to build a sustainable and neighborly environment where individuals can seamlessly collaborate, 
        freely share culinary wisdom, and wholeheartedly savor the joy of food together. Join us in this enriching mission!
        </p>

        {!auth.user ? <div className="landingbutton-container">
          <Link to="/register">
            <Button variant="primary">Register Now to Join The Cause!</Button>
          </Link>
        </div> : null}
        
      </div>




      </>

    )

}

export default LandingPage;