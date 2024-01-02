import MapHook from "../hooks/MapHook";
import LoginHook from "../hooks/LoginHook";
import "./MapRoute.css";

import React, { useState } from "react";
const MapRoute = () => {
  const [accessToken, setAccessToken] = useState(null);
  const [updating, setUpdating] = useState(false);
  const [feedId, setFeedId] = useState(null);

  return accessToken ? (
    <MapHook accessToken={accessToken} updating={updating} feedId={feedId} />
  ) : (
    <LoginHook
      setAccessToken={setAccessToken}
      setUpdating={setUpdating}
      setFeedId={setFeedId}
    />
  );
};

export default MapRoute;
