import logo from './logo.svg';
import { Navbar } from 'react-bootstrap';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import NavBar from './components/NavBar';
import RecipeCards from './components/Recipe/RecipeCards';
import Login from './components/Auth/Login';
import { useEffect, useState } from 'react';
import AuthContext from './context/AuthContext';
import jwtDecode from 'jwt-decode';
import RegistrationForm from './components/Auth/RegistrationForm';
import Recipe from './components/Recipe/Recipe';
import LandingPage from './components/LandingPage';
import RecipeForm from './components/Recipe/RecipeForm';

import ConfirmDelete from './components/Recipe/RecipeDelete';
import Pantry from './components/Pantry/Pantry';
import RecipeDelete from './components/Recipe/RecipeDelete';
import UploadImageToS3WithNativeSdk from './ImageUpload/FileUploadSample';

import RecipeCardsFromPantry from './components/Pantry/RecipePantry';

import LoFiMusic from './components/LoFiMusic';
import LikedRecipes from './components/Recipe/LikedRecipes';
import FourOhFour from './components/Error/404';






const LOCAL_STORAGE_TOKEN_KEY = "appUserToken";


function App() {

  

  const [user, setUser] = useState(null);
  const [restoreLoginAttemptCompleted, setRestoreLoginAttemptCompleted] = useState(false);

 // NEW: Define a useEffect hook callback function to attempt
  // to restore the user's token from localStorage
  useEffect(() => {
    const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
    if (token) {
      login(token);
    }
    setRestoreLoginAttemptCompleted(true);
  }, []);

  const login = (token) => {

    localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, token);

    // decode token
    const { sub: username, authorities: authoritiesString } = jwtDecode(token);

    // split the authorities string into an array of roles
    const roles = authoritiesString.split(',');

    // create the 'user' object
    const user = {
      username,
      roles,
      token,
      hasRole(role) {
        return this.roles.includes(role);
      }
    };


    // update the user state
    setUser(user);

    // return the user to the caller
    return user;
  };

  const logout = () => {
    setUser(null);
    // NEW: remove the token from localStorage
    localStorage.removeItem(LOCAL_STORAGE_TOKEN_KEY);
    // works similar to navigate but works outside of components
    window.location.href = '/';
  };

  const auth = {
    user: user ? {...user} : null,
    login,
    logout
  };

  // NEW: Dont render App component if nobody has attempted to log in yet
  if (!restoreLoginAttemptCompleted) {
    return null;
  }

  return (
    <AuthContext.Provider value={auth}>
      <div className="App">
        <NavBar />

        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<RegistrationForm />} />
            <Route path="/recipes" element={<RecipeCards />} />
            <Route path="/add-recipe" element={<RecipeForm />} />
            <Route path="/delete/:id" element={<RecipeDelete />} />
            <Route path="/recipes/:id" element={<Recipe />} />
            <Route path="/pantry" element={<Pantry />} />
            <Route path="/pantry/delete/:id" element={<Pantry />} />
            <Route path="/music" element={<LoFiMusic />} />
            <Route path="*" element={<FourOhFour />} /> 
            <Route path="/fileUpload" element={<UploadImageToS3WithNativeSdk />} />
            <Route path="/recipe-pantry" element={<RecipeCardsFromPantry />} />
            <Route path="/liked" element={<LikedRecipes />} />

          </Routes>
        </BrowserRouter>
      
      </div>
    </AuthContext.Provider>
  );
}

export default App;
