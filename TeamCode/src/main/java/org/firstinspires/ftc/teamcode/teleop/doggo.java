package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Projects.hi;
import org.firstinspires.ftc.teamcode.camera.AprilTagAutonomousInitDetectionExample;
import org.firstinspires.ftc.teamcode.camera.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
@TeleOp(name = "doggo")
public class doggo extends LinearOpMode{
    public hi robot = new hi();
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;

    static final double FEET_PER_METER = 3.28084;

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;

    // UNITS ARE METERS
    double tagsize = 0.166;

    // Tag ID 1,2,3 from the 36h11 family
    int Left = 1;
    int Middle = 2;
    int Right = 3;
    AprilTagDetection tagOfInterest = null;

    @Override
    public void runOpMode()
    {



        robot.init(hardwareMap);
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);

        /*
         * The INIT-loop:
         * This REPLACES waitForStart!
         */

        while (!isStarted() && !isStopRequested())
        {



            robot.rightLift.setTargetPosition(0);
            robot.rightLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            robot.leftLift.setTargetPosition(0);
            robot.leftLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.leftLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

            if (currentDetections.size() != 0)
            {
                boolean tagFound = false;

                for(AprilTagDetection tag : currentDetections)

                    if(tag.id == Left  || tag.id == Middle || tag.id == Right )
                    {
                        tagOfInterest = tag;
                        tagFound = true;
                        break;
                    }

                if(tagFound)
                {
                    telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                    tagToTelemetry(tagOfInterest);
                }
                else
                {
                    telemetry.addLine("Don't see tag of interest :(");

                    if(tagOfInterest == null)
                    {
                        telemetry.addLine("(The tag has never been seen)");
                    }
                    else
                    {
                        telemetry.addLine("\nBut we HAVE seen the tag before; last seen at:");
                        tagToTelemetry(tagOfInterest);
                    }
                }

            }
            else
            {
                telemetry.addLine("Don't see tag of interest\nlol, get better:(");

                if(tagOfInterest == null)
                {
                    telemetry.addLine("(The tag has never been seen in the history of this run, the records must be incomplete)");
                }
                else
                {
                    telemetry.addLine("\nBut we thankfully HAVE seen the tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }

            }

            telemetry.update();
            sleep(20);
        }

        /*
         * The START command just came in: now work off the latest snapshot acquired
         * during the init loop.
         */

        /* Update the telemetry */
        if(tagOfInterest != null)
        {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        }
        else
        {
            telemetry.addLine("No tag snapshot available, it was never sighted during the init loop :(");
            telemetry.update();
        }

        double x = tagOfInterest.pose.x*FEET_PER_METER;
        double y = tagOfInterest.pose.y*FEET_PER_METER;

        while (x > .5 || y > .5) {
            if (Math.toDegrees(tagOfInterest.pose.yaw) > 0) {
                turnRobot("right", Math.toDegrees(tagOfInterest.pose.yaw));
                while (x > .5 || y > .5) {
                    strafeRobot("right", x,y);
                }
            }
            else if (Math.toDegrees(tagOfInterest.pose.yaw) < 0) {
                turnRobot("left", Math.toDegrees(tagOfInterest.pose.yaw));
                while (x > .5 || y > .5) {
                    strafeRobot("left", x,y);
                }
            }
            else {
                while (x > .5 || y > .5) {
                    moveRobot(100, 1);
                }
            }

        }
        /* You wouldn't have this in your autonomous, this is just to prevent the sample from ending */
        while (opModeIsActive()) {sleep(20);}
    }


    void moveRobot(double time, double speed) {
        robot.fRightWheel.setPower(speed);
        robot.fLeftWheel.setPower(speed);
        robot.bRightWheel.setPower(speed);
        robot.bLeftWheel.setPower(speed);
        sleep((long) (time));
        robot.fRightWheel.setPower(0);
        robot.fLeftWheel.setPower(0);
        robot.bRightWheel.setPower(0);
        robot.bLeftWheel.setPower(0);
    }

    void turnRobot(String direction, double degrees) {
        if (direction == "right") {
            robot.fRightWheel.setPower(-.5);
            robot.bRightWheel.setPower(-.5);
            robot.fLeftWheel.setPower(.5);
            robot.bLeftWheel.setPower(.5);
            sleep((long) (degrees / 45 * 500));
            robot.fRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
        }
        if (direction == "left") {
            robot.fRightWheel.setPower(.5);
            robot.bRightWheel.setPower(.5);
            robot.fLeftWheel.setPower(-.5);
            robot.bLeftWheel.setPower(-.5);
            sleep((long) (degrees / 45 * 650));
            robot.fRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bRightWheel.setPower(0);
            robot.bLeftWheel.setPower(0);
        }

    }
    void strafeRobot(String side, double x, double y) {
        if (side == "right") {
            robot.fRightWheel.setPower(0);
            robot.bRightWheel.setPower(x);
            robot.fLeftWheel.setPower(y);
            robot.bLeftWheel.setPower(0);
        }
        else if (side == "left") {
            robot.fRightWheel.setPower(x);
            robot.bRightWheel.setPower(0);
            robot.fLeftWheel.setPower(0);
            robot.bLeftWheel.setPower(y);
        }

    }
    void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("Translation X: %.2f feet", detection.pose.x*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Y: %.2f feet", detection.pose.y*FEET_PER_METER));
        telemetry.addLine(String.format("Translation Z: %.2f feet", detection.pose.z*FEET_PER_METER));
        telemetry.addLine(String.format("Rotation Yaw: %.2f degrees", Math.toDegrees(detection.pose.yaw)));
        telemetry.addLine(String.format("Rotation Pitch: %.2f degrees", Math.toDegrees(detection.pose.pitch)));
        telemetry.addLine(String.format("Rotation Roll: %.2f degrees", Math.toDegrees(detection.pose.roll)));
    }
}
