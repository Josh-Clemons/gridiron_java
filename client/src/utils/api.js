import axios from 'axios';

export const fetchAvailableLeagues = async ({queryKey}) => {
    const [_key, {accessToken}] = queryKey;
    const response = await axios.get('http://localhost:8080/api/league/available', {
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    });
    if (!response.data) {
        throw new Error('No data!');
    }
    return response.data;
};

export const fetchLeagueDetails = async ({queryKey}) => {
    const [_key, {accessToken, leagueId}] = queryKey;
    const response = await axios.get(`http://localhost:8080/api/league/find-by-id?leagueId=${leagueId}`, {
        headers: {
            'Authorization': `Bearer ${accessToken}`
        }
    });
    if (!response.data) {
        throw new Error('No data!');
    }
    return response.data;
};
