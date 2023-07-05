import { useContext, useEffect, useState } from "react";
import useLeaguePicks from "../hooks/useLeaguePicks";
import { useParams } from "react-router-dom";
import { UserContext } from "../contexts/UserContext";
import { CompetitorContext } from "../contexts/CompetitorContext";
import WeeklyPicks from "../components/WeeklyPicks/WeeklyPicks";

const LeagueDetailsPage = () => {

  const { leagueId } = useParams()
  const { user } = useContext(UserContext);
  const { competitors } = useContext(CompetitorContext);

  const { data: picks, isLoading, isError, error } = useLeaguePicks(leagueId);

  const [myPicks, setMyPicks] = useState(null);
  const [leagueDetails, setLeagueDetails] = useState(null);


  useEffect(() => {
    setMyPicks(picks?.filter(pick => pick.owner.id === user.id).map(pick => {
      return ({
        id: pick.id,
        team: pick.competitor ? pick.competitor.team.abbreviation : "",
        leagueId: pick.league?.id,
        value: pick.value,
        week: pick.week
      })
    }));
    setLeagueDetails(picks && picks[0].league)
  }, [picks, user.id])


  return (
    <>
      {isLoading && <div>loading....</div>}
      {isError && <div>error getting league picks {error.message}</div>}
      <h2>LeagueId: {JSON.stringify(leagueId)}</h2>
      <h2>Owner: {picks && JSON.stringify(picks[0].owner.username)}</h2>
      {/* WeeklyPicks components for each week */}
      {Array.from({ length: 18 }, (_, i) => (
        <>
          <h3>Week: {i + 1}</h3>
          <WeeklyPicks week={i + 1} key={i} picks={myPicks} setPicks={setMyPicks} />
        </>
      ))}

      <div>League Details: {JSON.stringify(myPicks)}</div>
      {competitors && JSON.stringify(competitors)}
    </>
  )
}

export default LeagueDetailsPage;