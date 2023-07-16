// Libraries
import {useState, useEffect, useContext} from 'react';
import Box from '@mui/material/Box';
import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography} from "@mui/material";
import PropTypes from 'prop-types';
import {ThreeCircles} from 'react-loader-spinner';

// Contexts
import {UserContext} from "../../contexts/UserContext.jsx";

// Components
import PickSelect from "../PickSelect/PickSelect";
import SavePicksButton from "../Buttons/SavePicksButton";

// Styles
import {StyledTableRow} from "../../styles/SharedStyles";

const PickSelections = ({picks, setPicks, leagueScores}) => {
    // Hooks
    const {user} = useContext(UserContext);
    const [loading, setLoading] = useState(true);
    const [myScore, setMyScore] = useState(0);

    // Side Effects
    useEffect(() => {
        // Update the user's score whenever leagueScores changes
        setMyScore(leagueScores.find(score => score.username === user.username));
    }, [leagueScores]);

    useEffect(() => {
        // Stop the loading state once picks have been loaded
        if (picks) setLoading(false);
    }, [picks]);

    if (loading) {
        return (
            <Box sx={{display: 'flex', height: '100vh', mt: 5}}>
                <ThreeCircles
                    type="ThreeDots"
                    color="#5BC0BE"
                    height={100}
                    width={100}
                />
            </Box>
        );
    }

    return (
        <Box width={'100%'} display={'flex'} flexDirection={'column'} alignItems={'center'}>
            <SavePicksButton updatedPicks={picks} width={250}/>
            <Typography variant={'h6'}>Total Score: {myScore.score}</Typography>
            <TableContainer component={Paper}>
                <Table size='small' sx={{width: '100%'}}>
                    <TableHead>
                        <TableRow>
                            <TableCell>Week</TableCell>
                            <TableCell>5 pts</TableCell>
                            <TableCell>3 pts</TableCell>
                            <TableCell>1 pt</TableCell>
                            <TableCell>Score</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {Array.from({length: 18}, (_, i) => (
                            <StyledTableRow key={i}>
                                <TableCell>{i + 1}</TableCell>
                                <TableCell>
                                    <PickSelect week={i + 1} picks={picks} setPicks={setPicks} value={5}/>
                                </TableCell>
                                <TableCell>
                                    <PickSelect week={i + 1} picks={picks} setPicks={setPicks} value={3}/>
                                </TableCell>
                                <TableCell>
                                    <PickSelect week={i + 1} picks={picks} setPicks={setPicks} value={1}/>
                                </TableCell>
                                <TableCell>
                                    {picks.filter(p => p.week === i + 1 && p.isWinner)
                                        .reduce((total, pick) => total + pick.value,
                                            (picks.filter(p => p.week === i + 1 && p.isWinner).length === 3 ? 2 : 0)
                                        )
                                    }
                                </TableCell>
                            </StyledTableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            <SavePicksButton updatedPicks={picks} width={250}/>
        </Box>
    )
}

PickSelections.propTypes = {
    picks: PropTypes.array,
    setPicks: PropTypes.func,
    leagueScores: PropTypes.array
}

export default PickSelections;