import { useContext } from 'react';
import LoginForm from '../components/LoginForm/LoginForm';
import { UserContext } from '../contexts/UserContext';

const DashboardPage = () => {

    const { user } = useContext(UserContext);

    return (
        <div>Hello {user?.username}</div>
    )
}

export default DashboardPage;