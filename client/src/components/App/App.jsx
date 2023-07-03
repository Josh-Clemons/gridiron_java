import { HashRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import { UserProvider } from '../../contexts/UserContext';

import PrivateRoute from '../PrivateRoute/PrivateRoute.jsx'
import TestApi from "../../assets/TestApi"
import LandingPage from '../../pages/LandingPage.jsx';
import LoginPage from '../../pages/LoginPage.jsx';
import DashboardPage from '../../pages/DashboardPage';

const queryClient = new QueryClient;

function App() {


  return (
    <QueryClientProvider client={queryClient}>
      <UserProvider>
        <Router>
          <Routes>
            {/* Unprotected routes */}
            <Route path="/" element={<Navigate replace to="/landing" />} />
            <Route path='/landing' element={<LandingPage />} />
            <Route path='/login' element={<LoginPage />} />
            {/* Protected routes */}
            <Route path='/dashboard' element={<PrivateRoute><DashboardPage /></PrivateRoute>} />
            <Route path='/test' element={<PrivateRoute><TestApi /></PrivateRoute>} />
          </Routes>
        </Router>
      </UserProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
