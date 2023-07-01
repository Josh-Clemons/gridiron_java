import { HashRouter as Router, Route, Routes, Navigate } from 'react-router-dom';

import TestApi from "../../assets/TestApi"
import Home from '../../pages/Home';

function App() {

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate replace to="/login" />} />
        <Route path='/login' element={<TestApi/>}/>
        <Route path='/home' element={<Home/>}/>
      </Routes>
    </Router>
  )
}

export default App
