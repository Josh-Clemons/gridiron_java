import { useContext, useEffect, useState } from "react";
import useLeaguePicks from "../hooks/useLeaguePicks";
import { useParams } from "react-router-dom";
import { UserContext } from "../contexts/UserContext";
import useSavePicks from "../hooks/useSavePicks.js";
import { useQuery } from "react-query";
import { fetchLeagueDetails } from "../utils/api.js";
import LeagueStandings from "../components/LeagueStandings/LeagueStandings.jsx";
import LeagueDetails from "../components/LeagueDetails/LeagueDetails";
import PickSelections from "../components/PickSelections/PickSelections";

const LeagueDetailsPage = () => {

  const { leagueId } = useParams()
  const { user } = useContext(UserContext);

  const { data: picks, isLoadingPicks } = useLeaguePicks(leagueId);
  const { data: leagueDetails }
    = useQuery(['leagueDetails', {
      accessToken: user.accessToken,
      leagueId: leagueId
    }], fetchLeagueDetails, { refetchOnWindowFocus: false });
  const [myPicks, setMyPicks] = useState();
  const [leagueScores, setLeagueScores] = useState([]);
  const [isLeagueOwner, setIsLeagueOwner] = useState(false);
  const [isLeagueMember, setIsLeagueMember] = useState(false);

  const savePicksMutation = useSavePicks();

  useEffect(() => {
    let leagueScores = [];
    const usernames = Array.from(
      new Set(
        picks?.map((pick) => pick.owner.username)
      )
    );
    for (let username of usernames) {
      const tempUserPicks = picks.filter(pick => pick.owner.username === username)
      let score = 0;
      for (let pick of tempUserPicks) {
        if (pick?.competitor?.winner) score = score + pick.value;
      }
      leagueScores.push({ username: username, score: score });
    }
    setLeagueScores(leagueScores);
    setMyPicks(picks?.filter(pick => pick.owner.id === user.id).map(pick => {
      return ({
        id: pick.id,
        ownerId: user.id,
        team: pick.competitor ? pick.competitor.team.abbreviation : "",
        leagueId: pick.league?.id,
        value: pick.value,
        week: pick.week
      })
    }));
    if (picks?.length > 0) {
      if (picks[0].league.leagueOwner === user.username) {
        setIsLeagueOwner(true);
      } else if (picks[0].owner.id === user.id) {
        setIsLeagueMember(true);
      } else {
        setIsLeagueOwner(false);
        setIsLeagueMember(false);
      }
    }
  }, [picks, user.id, user.username])


  const savePicks = (updatedPicks) => {
    console.log('in savePicks, myPicks:', updatedPicks)
    savePicksMutation.mutate(updatedPicks);
  };

  if (isLoadingPicks) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <LeagueDetails isMember={isLeagueMember} isOwner={isLeagueOwner} leagueDetails={leagueDetails} />

      <LeagueStandings leagueScores={leagueScores} />
      <button onClick={() => savePicks(myPicks)}>Save</button>
      {/* WeeklyPicks components for each week */}
      {picks ? <PickSelections picks={myPicks} setPicks={setMyPicks} /> : null }
      <div>isMember: {JSON.stringify(isLeagueMember)}, isOwner: {JSON.stringify(isLeagueOwner)}</div>
      <div>Scores: {JSON.stringify(leagueScores)}</div>
      <div>League: {JSON.stringify(leagueDetails)}</div>
      <div>My Picks: {JSON.stringify(myPicks)}</div>
      <div>User: {JSON.stringify(user)}</div>
      <div>Picks: {JSON.stringify(picks)}</div>
    </>
  )
}

export default LeagueDetailsPage;