import React, { useRef } from "react";
import { Map } from "react-map-gl";

const MAPBOX_TOKEN =
  "pk.eyJ1IjoiZHJvdzcyNCIsImEiOiJjbGI3dGpiZ3AwZGRvM3NvMnU5a2w3ZHh4In0.Ei81FJmfrOdiB2Rn2rlKyA";

const MapComponent = ({ setting, pins = {}, popUp = {} }) => {
  console.log(pins);
  const { initialViewState, interactiveLayerIds, onClick } = setting;
  return (
    <React.Fragment>
      <Map
        mapboxAccessToken={MAPBOX_TOKEN}
        style={{ width: "100%", height: "100vh" }}
        mapStyle={"mapbox://styles/mapbox/dark-v11"}
        initialViewState={initialViewState}
        interactiveLayerIds={interactiveLayerIds}
        onClick={onClick}
      >
        {pins}
        {popUp}
      </Map>
    </React.Fragment>
  );
};

export default MapComponent;
