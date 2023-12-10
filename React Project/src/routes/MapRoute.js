import MapHook from "../hooks/MapHook";
import LoginHook from "../hooks/LoginHook";
import "./MapRoute.css";

import React, { useEffect, useMemo, useRef, useState } from "react";
const MapRoute = () => {
  const currentUrl = window.location.href;
  const searchParams = new URL(currentUrl).searchParams;
  const code = searchParams.get("code");

  const isLogin = code ? true : false;
  return isLogin ? <MapHook /> : <LoginHook />;
};

export default MapRoute;
