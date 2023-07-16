// Libraries
import {
    Box,
    Paper,
    Table,
    TableContainer,
    TableBody,
    TableHead,
    TableRow,
    TableCell,
    Typography
} from "@mui/material";
import PropTypes from 'prop-types';
import { StyledTableRow } from '../../styles/SharedStyles';

const LeagueStandings = ({ leagueScores }) => {

    return (
        <Box width={'100%'}>
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
                        {leagueScores?.map((score, i) => {
                            return (
                                <StyledTableRow key={i}>
                                    <TableCell width={20}>{i + 1}</TableCell>
                                    <TableCell><Typography variant='body1'
                                        noWrap={true}>{score.username}</Typography></TableCell>
                                    <TableCell align='right'>{score.score}</TableCell>
                                </StyledTableRow>)
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