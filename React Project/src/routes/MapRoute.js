import MapHook from "../hooks/MapHook";
import LoginHook from "../hooks/LoginHook";
import "./MapRoute.css";

import React, { useState } from "react";
const MapRoute = () => {
  const [accessToken, setAccessToken] = useState(null);
  const [updating, setUpdating] = useState(false);
  const [id, setId] = useState(null);

  return accessToken ? (
    <MapHook accessToken={accessToken} updating={updating} id={id} />
  ) : (
    <LoginHook
      setAccessToken={setAccessToken}
      setUpdating={setUpdating}
      setId={setId}
    />
  );
};

export default MapRoute;
