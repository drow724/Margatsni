import React, { forwardRef, useRef } from "react";
import { clusterLayer } from "../components/layers";
import { Map, Popup } from "react-map-gl";
import SwipeableTextMobileStepper from "../components/SwipeableTextMobileStepper";
import { CircularProgress } from "@mui/material";
import { Box } from "@mui/system";

const MAPBOX_TOKEN =
  "pk.eyJ1IjoiZHJvdzcyNCIsImEiOiJjbGI3dGpiZ3AwZGRvM3NvMnU5a2w3ZHh4In0.Ei81FJmfrOdiB2Rn2rlKyA";

const MapComponent = (
  { loading, bounds, onClick, pins, popupInfo, setPopupInfo, bottom },
  mapRef
) => {
  return loading ? (
    <React.Fragment>
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          height: "100vh",
          width: "100%",
          justifyContent: "center",
          backgroundColor: "#292929",
        }}
      >
        <CircularProgress size={100} disableShrink />
      </Box>
    </React.Fragment>
  ) : (
    <React.Fragment>
      <Map
        initialViewState={{
          bounds,
          fitBoundsOptions: { padding: 40, duration: 1000 },
        }}
        mapboxAccessToken={MAPBOX_TOKEN}
        style={{ width: "100%", height: "100vh" }}
        mapStyle="mapbox://styles/mapbox/dark-v11"
        interactiveLayerIds={[clusterLayer.id]}
        ref={mapRef}
        onClick={onClick}
      >
        {pins}
        {popupInfo && (
          <React.Fragment>
            <Popup
              anchor="top"
              longitude={Number(popupInfo.location.lng)}
              latitude={Number(popupInfo.location.lat)}
              onClose={() => setPopupInfo(null)}
              style={{
                width: "100%",
                borderRadius: 10,
                alignItems: "center",
                justifyContent: "space-between",
              }}
            >
              <SwipeableTextMobileStepper info={popupInfo} />
            </Popup>
          </React.Fragment>
        )}
      </Map>
      {bottom}
    </React.Fragment>
  );
};

const wrapper = forwardRef((props, ref) => {
  return MapComponent(props, ref);
});

export default wrapper;
