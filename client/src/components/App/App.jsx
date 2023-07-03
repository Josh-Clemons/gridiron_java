import { HashRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query'
import { ReactQueryDevtools } from 'react-query/devtools'

import { UserProvider } from '../../contexts/UserContext';

import TestApi from "../../assets/TestApi"
import LandingPage from '../../pages/LandingPage.jsx';

const queryClient = new QueryClient;

function App() {

  return (
    <QueryClientProvider client={queryClient}>
      <UserProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Navigate replace to="/login" />} />
            <Route path='/login' element={<TestApi />} />
            <Route path='/home' element={<LandingPage />} />
          </Routes>
        </Router>
      </UserProvider>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  )
}

export default App
