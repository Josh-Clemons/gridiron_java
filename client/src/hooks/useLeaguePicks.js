import { useContext } from "react";
import { useQuery } from "react-query";
import { UserContext } from "../contexts/UserContext";
import axios from 'axios';

function useLeaguePicks(leagueId) {
  const {user} = useContext(UserContext);

  return useQuery(['leaguePicks', leagueId], async () => {
    const response = await axios.get(`http://localhost:8080/api/pick/all-league-picks?leagueId=${leagueId}`, {
      headers: {
        'Authorization': `Bearer ${user.accessToken}`
      }
    })

    // TODO better error response
    if (response.status !== 200) {
      throw new Error('Error fetching league picks');
    }

    return response.data
  })
}

export default useLeaguePicks;