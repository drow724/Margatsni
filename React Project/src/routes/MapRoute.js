import MapHook from "../hooks/MapHook";
import LoginHook from "../hooks/LoginHook";
import "./MapRoute.css";

import React, { useState } from "react";
const MapRoute = () => {
  const [accessToken, setAccessToken] = useState(null);

  return accessToken ? (
    <MapHook accessToken={accessToken} />
  ) : (
    <LoginHook setAccessToken={setAccessToken} />
  );
};

export default MapRoute;
