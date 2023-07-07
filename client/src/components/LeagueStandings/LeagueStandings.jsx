import PropTypes from 'prop-types';

import {
    Box,
    Paper,
    Table,
    TableContainer,
    TableBody,
    TableHead,
    TableRow,
    TableCell,
    Typography,
} from "@mui/material";

const LeagueStandings = ({ leagueScores }) => {
    // styles for the table rows
    // const StyledTableRow = styled(TableRow)(() => ({
    //     '&:nth-of-type(odd)': {
    //         backgroundColor: "#1C2541",
    //     },
    //     '&:nth-of-type(even)': {
    //         backgroundColor: "#242f53",
    //     },
    // }));


    return (
        <Box width={'100%'} mb={'80px'}>
            <TableContainer component={Paper} sx={{ width: '100%' }}>
                <Table size='small' sx={{ width: '100%' }}>
                    <TableHead>
                        <TableRow>
                            <TableCell width={20}>Rank</TableCell>
                            <TableCell>Username</TableCell>
                            <TableCell align='right'>Score</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {leagueScores.map((score, i) => {
                            return (
                                <TableRow key={i}>
                                    <TableCell width={20}>{i + 1}</TableCell>
                                    <TableCell><Typography variant='body1'
                                        noWrap={true}>{score.username}</Typography></TableCell>
                                    <TableCell align='right'>{score.score}</TableCell>
                                </TableRow>)
                        })}
                    </TableBody>
                </Table>
            </TableContainer>


        </Box>
    );
}

LeagueStandings.propTypes = {
    leagueScores: PropTypes.array
}
export default LeagueStandings;