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
    styled
} from "@mui/material";

const LeagueStandings = ({leagueScores}) => {
// styles for the table rows
    const StyledTableRow = styled(TableRow)(() => ({
        '&:nth-of-type(odd)': {
            backgroundColor: "#1C2541",
        },
        '&:nth-of-type(even)': {
            backgroundColor: "#242f53",
        },
    }));


    return (
        <Box width={'100%'} mb={'80px'}>
            <TableContainer component={Paper} sx={{width: '100%'}}>
                <Table size='small' sx={{width: '100%'}}>
                    <TableHead>
                        <TableRow>
                            <TableCell width={20}>Rank</TableCell>
                            <TableCell>Username</TableCell>
                            <TableCell align='right'>Score</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {leagueScores.map((user, i) => {
                            return (
                                <TableRow key={i}>
                                    <TableCell width={20}>{i + 1}</TableCell>
                                    <TableCell><Typography variant='body1'
                                                           noWrap={true}>{user.username}</Typography></TableCell>
                                    <TableCell align='right'>{user.score}</TableCell>
                                </TableRow>)
                        })}
                    </TableBody>
                </Table>
            </TableContainer>


        </Box>
    );
}

export default LeagueStandings;