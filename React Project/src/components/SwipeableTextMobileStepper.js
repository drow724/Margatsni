import MobileStepper from "@mui/material/MobileStepper";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import KeyboardArrowLeft from "@mui/icons-material/KeyboardArrowLeft";
import KeyboardArrowRight from "@mui/icons-material/KeyboardArrowRight";
import * as React from "react";
import SwipeableViews from "react-swipeable-views";
import { autoPlay } from "react-swipeable-views-utils";
import { useTheme } from "@mui/system";
import { CircularProgress } from "@mui/material";
import axios from "axios";

const AutoPlaySwipeableViews = autoPlay(SwipeableViews);

function SwipeableTextMobileStepper({ info }) {
  const theme = useTheme();
  const [activeStep, setActiveStep] = React.useState(0);
  const [images, setImages] = React.useState([]);
  const maxSteps = info.images.length;
  const [loaded, setLoaded] = React.useState(0);
  const [isLoaded, setIsLoaded] = React.useState(false);

  const getImages = async (url) => {
    return await axios({
      method: "post",
      url: `http://146.56.38.5:8080/image`,
      data: url,
      responseType: "blob",
    });
  };

  React.useEffect(() => {
    info.images.forEach((image) => {
      getImages(image.url).then((response) => {
        const url = window.URL.createObjectURL(response.data);
        const img = React.createElement(Box, {
          src: url,
          component: "img",
          sx: {
            height: "100%",
            display: "block",
            maxWidth: 400,
            overflow: "hidden",
            width: "100%",
          },
          style: { WebkitUserDrag: "none" },
        });
        setLoaded((current) => current + 1);
        setImages((current) => [...current, img]);
      });
    });
  }, [info.images]);

  React.useEffect(() => {
    setIsLoaded(loaded >= maxSteps);
  }, [loaded, maxSteps]);

  const handleNext = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleStepChange = (step) => {
    setActiveStep(step);
  };

  return (
    <Box
      sx={{
        flexGrow: 1,
      }}
    >
      {
        <React.Fragment>
          <Paper
            square
            elevation={0}
            sx={{
              display: "flex",
              alignItems: "center",
              height: 50,
              pl: 2,
              bgcolor: "background.default",
            }}
          >
            <Typography>
              {info.location.city ? info.location.city : null}{" "}
              {info.location.city && info.location.name ? "," : null}{" "}
              {info.location.name ? ` ${info.location.name}` : null}
            </Typography>
          </Paper>
          <CircularProgress
            style={{
              display: isLoaded ? "none" : "inline",
              color: "#808080",
            }}
            color="inherit"
          />
          <AutoPlaySwipeableViews
            axis={theme.direction === "rtl" ? "x-reverse" : "x"}
            index={activeStep}
            onChangeIndex={handleStepChange}
            enableMouseEvents
            style={{
              WebkitUserDrag: "none",
              display: isLoaded ? "block" : "none",
            }}
            autoplay={isLoaded}
          >
            {images.map((image, i) => (
              <div key={i + image.src}>{image}</div>
            ))}
          </AutoPlaySwipeableViews>
          <MobileStepper
            steps={maxSteps}
            position="static"
            activeStep={activeStep}
            nextButton={
              <Button
                size="small"
                onClick={handleNext}
                disabled={activeStep === maxSteps - 1}
              >
                Next
                {theme.direction === "rtl" ? (
                  <KeyboardArrowLeft />
                ) : (
                  <KeyboardArrowRight />
                )}
              </Button>
            }
            backButton={
              <Button
                size="small"
                onClick={handleBack}
                disabled={activeStep === 0}
              >
                {theme.direction === "rtl" ? (
                  <KeyboardArrowRight />
                ) : (
                  <KeyboardArrowLeft />
                )}
                Back
              </Button>
            }
          />
          <Typography variant="body2" gutterBottom>
            {info.caption}
          </Typography>
        </React.Fragment>
      }
    </Box>
  );
}

export default SwipeableTextMobileStepper;
