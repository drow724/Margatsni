import MapHook from "../hooks/MapHook";
import LoginHook from "../hooks/LoginHook";
import "./MapRoute.css";

const MapRoute = () => {
  const isLogin = true;
  return isLogin ? <MapHook /> : <LoginHook />;
};

export default MapRoute;
