package org.firstinspires.ftc.teamcode.Projects;
import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class hi extends Project{

    public.DcMotor fLeftWheel = null;
    public DcMotor fRightWheel = null;
    public DcMotor bLeftWheel = null;
    public DcMotor bRightWheel = null;
    public DcMotor scissorLift = null;
    public Servo rClaw = null;
    public Servo lClaw = null;


    // public DcMotor intake = null;


    @Override
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        fLeftWheel = hwMap.dcMotor.get("fLeftWheel");
        fRightWheel = hwMap.dcMotor.get("fRightWheel");
        bLeftWheel = hwMap.dcMotor.get("bLeftWheel");
        bRightWheel = hwMap.dcMotor.get("bRightWheel");
	    scissorLift = hwMap.dcMotor.get("scissorLife");
        rClaw = hwMap.servo.get("rClaw");
        lClaw = hwMap.servo.get("lClaw");

        // intake = hwMap.dcMotor.get("intake"); //1


        // Motors and facing in to each other
        fRightWheel.setDirection(DcMotor.Direction.FORWARD);
        fLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        bRightWheel.setDirection(DcMotor.Direction.FORWARD);
        bLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        scissorLift.setDirection(DcMotor.Direction.FORWARD);

        // intake.setDirection(DcMotor.Direction.FORWARD);


        fRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        scissorLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        // intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        fRightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fLeftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bRightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bLeftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        scissorLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        // intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        Stop();
    }

    public void Stop(){
        fRightWheel.setPower(0);
        fLeftWheel.setPower(0);
        bRightWheel.setPower(0);
        bLeftWheel.setPower(0);
        scissorLift.setPower(0);
        rClaw.setPosition(1);
        lClaw.setPosition(0);


    }
}
