import { Face } from "@mui/icons-material";
import React, { useRef, useState, useMemo } from "react";
import { Map, Layer } from "react-map-gl";
import {
  FacebookLoginButton,
  TwitterLoginButton,
} from "react-social-login-buttons";

const MAPBOX_TOKEN =
  "pk.eyJ1IjoiZHJvdzcyNCIsImEiOiJjbGI3dGpiZ3AwZGRvM3NvMnU5a2w3ZHh4In0.Ei81FJmfrOdiB2Rn2rlKyA";

function MapHook() {
  const [lng, setLng] = useState(-70.9);
  const [lat, setLat] = useState(42.35);
  const [zoom, setZoom] = useState(2);

  const mapRef = useRef();

  const logins = (
    <Layer
      anchor="bottom"
      onClick={(e) => {
        // If we let the click event propagates to the map, it will immediately close the popup
        // with `closeOnClick: true`
        e.originalEvent.stopPropagation();
      }}
    >
      <FacebookLoginButton />
    </Layer>
  );
  return (
    <React.Fragment>
      <Map
        initialViewState={{
          longitude: lng,
          latitude: lat,
          zoom: zoom,
        }}
        ref={mapRef}
        mapboxAccessToken={MAPBOX_TOKEN}
        style={{ width: "100%", height: "100vh", position: "fixed" }}
        mapStyle="mapbox://styles/mapbox/dark-v11"
      ></Map>
      <div
        style={{
          position: "fixed",
          width: "20%",
          height: "100%",
          margin: "0 auto",
          left: 0,
          right: 0,
          bottom: 0,
          top: 0,
          display: "flex",
          justifyContent: "center",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <FacebookLoginButton />
        <TwitterLoginButton />
      </div>
    </React.Fragment>
  );
}

export default MapHook;
