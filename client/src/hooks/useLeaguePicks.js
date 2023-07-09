import {useContext} from "react";
import {useQuery} from "react-query";
import {UserContext} from "../contexts/UserContext";
import axios from 'axios';

function useLeaguePicks(code) {
    const {user} = useContext(UserContext);

    return useQuery(['leaguePicks', code], async () => {
        const response = await axios.get(`http://localhost:8080/api/pick/all-league-picks?inviteCode=${code}`, {
            headers: {
                'Authorization': `Bearer ${user.accessToken}`
            }
        })

        // TODO better error response
        if (response.status !== 200) {
            throw new Error('Error fetching league picks');
        }

        return response.data
    }, {
        refetchOnWindowFocus: false,
    })
}

export default useLeaguePicks;