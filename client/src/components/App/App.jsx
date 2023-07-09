import { HashRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'
import { ToastContainer, Zoom } from 'react-toastify';

import { UserProvider } from '../../contexts/UserContext';

import CssBaseline from '@mui/material/CssBaseline';
import 'react-toastify/dist/ReactToastify.css';

import PrivateRoute from '../PrivateRoute/PrivateRoute.jsx'
import TestApi from "../../assets/TestApi"
import LandingPage from '../../pages/LandingPage.jsx';
import LoginPage from '../../pages/LoginPage.jsx';
import DashboardPage from '../../pages/DashboardPage';
import { RegisterPage } from '../../pages/RegisterPage';
import LeagueDetailsPage from '../../pages/LeagueDetailsPage';
import { CompetitorProvider } from '../../contexts/CompetitorContext';
import FindLeaguePage from "../../pages/FindLeaguePage.jsx";
import NavBar from '../NavBar/NavBar';
import CreateLeaguePage from '../../pages/CreateLeaguePage';

const queryClient = new QueryClient;

function App() {


  return (
    <QueryClientProvider client={queryClient}>
      <UserProvider>
        <CompetitorProvider>
          <CssBaseline />
          <Router>
            <NavBar />
            <Routes>
              {/* Unprotected routes */}
              <Route path="/" element={<Navigate replace to="/landing" />} />
              <Route path='/landing' element={<LandingPage />} />
              <Route path='/login' element={<LoginPage />} />
              <Route path='/register' element={<RegisterPage />} />
              {/* Protected routes */}
              <Route path='/dashboard' element={<PrivateRoute><DashboardPage /></PrivateRoute>} />
              <Route path='/test' element={<PrivateRoute><TestApi /></PrivateRoute>} />
              <Route path='/league-detail/:inviteCode' element={<PrivateRoute><LeagueDetailsPage /></PrivateRoute>} />
              <Route path='/find-league' element={<PrivateRoute><FindLeaguePage /></PrivateRoute>} />
              <Route path='/create' element={<PrivateRoute><CreateLeaguePage /></PrivateRoute>} />
            </Routes>
            <ToastContainer
              position="top-right"
              transition={Zoom}
              autoClose={5000}
              hideProgressBar={false}
              newestOnTop={false}
              closeOnClick
              rtl={false}
              pauseOnFocusLoss
              draggable
              pauseOnHover
              theme="dark"
            />
          </Router>
        </CompetitorProvider>
      </UserProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
