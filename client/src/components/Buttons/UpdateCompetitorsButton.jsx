// Libraries
import { Button } from '@mui/material';
import DoneAllIcon from '@mui/icons-material/DoneAll';
import {ThreeCircles} from "react-loader-spinner";

// Hooks
import {updateCompetitorData} from "../../utils/api.js";
import {useMutation} from "react-query";
import {useContext} from "react";
import {UserContext} from "../../contexts/UserContext.jsx";

const UpdateCompetitorsButton = () => {
    const {user} = useContext(UserContext);

    const mutation = useMutation(() => updateCompetitorData({accessToken: user.accessToken}));

    return (
        <>
            {mutation.isLoading ? (
                <ThreeCircles
                    color="#5BC0BE"
                    height={76}
                    width={70}
                />
            ) : (
                <Button variant='outlined'
                        size='large'
                        color='success'
                        onClick={() => mutation.mutate()}
                        sx={{ width: 250, mt: 2, mb: 2, borderWidth: '2px', '&:hover': { borderWidth: '2px' } }}>
                    Update Competitors<DoneAllIcon sx={{ ml: 2 }} />
                </Button>
            )}
        </>
    )
}

export default UpdateCompetitorsButton;