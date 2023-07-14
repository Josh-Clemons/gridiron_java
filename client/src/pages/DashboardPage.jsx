import {Dashboard} from '../components/Dashboard/Dashboard';
import useScrollToTop from "../hooks/useScrollToTop.js";

const DashboardPage = () => {
    useScrollToTop();
    return (
        <Dashboard/>
    )
}

export default DashboardPage;