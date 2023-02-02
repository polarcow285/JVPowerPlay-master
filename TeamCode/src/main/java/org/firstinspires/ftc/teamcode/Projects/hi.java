package org.firstinspires.ftc.teamcode.Projects;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class hi extends Project{

    public DcMotor fLeftWheel = null;
    public DcMotor fRightWheel = null;
    public DcMotor bLeftWheel = null;
    public DcMotor bRightWheel = null;
   public Servo rClaw = null;
    public Servo lClaw = null;
    public Servo wristServo = null;
    public Servo armServo1 = null;
    public Servo armServo2 = null;
    
   // public DcMotor intake = null;
   public DcMotor rightLift = null;
   public DcMotor leftLift = null;


    @Override
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        fLeftWheel = hwMap.dcMotor.get("fLeftWheel");
        fRightWheel = hwMap.dcMotor.get("fRightWheel");
        bLeftWheel = hwMap.dcMotor.get("bLeftWheel");
        bRightWheel = hwMap.dcMotor.get("bRightWheel");
        rClaw = hwMap.servo.get("rClaw");
        lClaw = hwMap.servo.get("lClaw");
        wristServo = hwMap.servo.get("wristServo");
        armServo1 = hwMap.servo.get("armServo1");
        armServo2 = hwMap.servo.get("armServo2");
       // intake = hwMap.dcMotor.get("intake"); //1
        rightLift = hwMap.dcMotor.get("rightLift"); //0
        leftLift = hwMap.dcMotor.get("leftLift");

        // Motors and facing in to each other
        fRightWheel.setDirection(DcMotor.Direction.FORWARD);
        fLeftWheel.setDirection(DcMotor.Direction.REVERSE);
        bRightWheel.setDirection(DcMotor.Direction.FORWARD);
        bLeftWheel.setDirection(DcMotor.Direction.REVERSE);

       // intake.setDirection(DcMotor.Direction.FORWARD);
        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotor.Direction.REVERSE);

        fRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       // intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fRightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fLeftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bRightWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bLeftWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
       // intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Stop();
    }

    public void Stop(){
        fRightWheel.setPower(0);
        fLeftWheel.setPower(0);
        bRightWheel.setPower(0);
        bLeftWheel.setPower(0);
        rClaw.setPosition(0);
        lClaw.setPosition(1);
      // intake.setPower(0);
        rightLift.setPower(0);
        leftLift.setPower(0);


    }
}
