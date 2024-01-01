import MapHook from "../hooks/MapHook";
import LoginHook from "../hooks/LoginHook";
import "./MapRoute.css";

import React, { useState } from "react";
const MapRoute = () => {
  const [accessToken, setAccessToken] = useState(null);
  const [updating, setUpdating] = useState(false);

  return accessToken ? (
    <MapHook accessToken={accessToken} updating={updating} />
  ) : (
    <LoginHook setAccessToken={setAccessToken} setUpdating={setUpdating} />
  );
};

export default MapRoute;
