import { createContext, useContext } from 'react';
import PropTypes from 'prop-types';
import { useQuery } from 'react-query';
import axios from 'axios';
import { UserContext } from './UserContext';

export const CompetitorContext = createContext();

export const CompetitorProvider = ({ children }) => {
  const { user } = useContext(UserContext);
  
  const fetchCompetitors = async () => {
    const { data } = await axios.get('http://localhost:8080/api/gamedata/competitors', {
      headers: {
        'Authorization': `Bearer ${user.accessToken}`
      }
    })
    return data;
  }

  const { data: competitors, isError, error, isLoading, refetch } = useQuery('competitors', fetchCompetitors, {
    retry: false,
    refetchOnWindowFocus: false,
  });

  return (
    <CompetitorContext.Provider value={{ competitors, isError, error, isLoading, refetch }}>
      {children}
    </CompetitorContext.Provider>
  )
}

CompetitorProvider.propTypes = {
  children: PropTypes.node,
}