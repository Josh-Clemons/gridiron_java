// Libraries
import axios from 'axios';
import PropTypes from 'prop-types';
import {createContext, useContext} from 'react';
import {useQuery} from 'react-query';

// Contexts
import {UserContext} from './UserContext';

export const CompetitorContext = createContext();

export const CompetitorProvider = ({children}) => {
    // Contexts
    const {user} = useContext(UserContext);

    // Functions
    const fetchCompetitors = async () => {
        const {data} = await axios.get(`${window.BACKEND_URL}/api/gamedata/competitors`, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}`
            }
        })
        return data;
    }

    // Query
    const {data: competitors, isError, error, isLoading, refetch} = useQuery('competitors', fetchCompetitors, {
        refetchOnWindowFocus: false,
    });

    return (
        <CompetitorContext.Provider value={{competitors, isError, error, isLoading, refetch}}>
            {children}
        </CompetitorContext.Provider>
    )
}

CompetitorProvider.propTypes = {
    children: PropTypes.node,
}