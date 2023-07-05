import { useContext } from "react";
import { useMutation } from "react-query";
import { UserContext } from "../contexts/UserContext";
import axios from 'axios';

function useSavePicks() {
    const {user} = useContext(UserContext);

    return useMutation((picks) => axios.post(`http://localhost:8080/api/pick/update`, picks, {
        headers: {
            'Authorization': `Bearer ${user.accessToken}`
        }
    }), {
        onError: (error) => {
            console.log("Error fetching league picks:", error);
        },
        onSuccess: (data) => {
            window.alert("Picks Saved");
            return data;
        }
    })
}

export default useSavePicks;