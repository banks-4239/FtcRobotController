/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name = "Basic: Auto OpMode", group = "Auto Opmode")
//@Disabled
public class BasicOpMode_Auto extends LinearOpMode {

    boolean choosingAuto = false;
    int autoMode = 0;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor robotArm = null;
    private DcMotor intake = null;
    private DcMotor spinner = null;

    double mmperin = 0.039701;

    int onSetting = 1;

    static int LIFT_5 = 5100; // 1850;
    static int LIFT_4 = 3444; // 1200;
    static int LIFT_3 = 1578; // 550;
    static int LIFT_2 = 947;  // 330;
    static int LIFT_1 = 344;  // 120;
    static int LIFT_0 = 00;

    boolean settingButtonDown1 = false;
    boolean settingButtonDown2 = false;

    double ticksperrotation = 537.7;
    double ticksperdegree = 1.06805555556;
    double wheeldiameter = 100;
    double pi = 3.1415;
    
    static final double SPINNER_SPEED = .2;
    
    static final double LIFT_ARM_ROTATE_PWR = 1;

    static final double FAST = 1;
    static final double MEDIUM = 0.6;
    static final double SLOW = 0.3;

    static final long SPIN_DURATION = 7500;

    boolean redOrBlue = true;

    int hubNum = 3;

    private static final String VUFORIA_KEY =
            "AWm4j/n/////AAABmTASu/sajk1hgar/ycfLY5JgGop7sElDWeK3soXHJ2uHPc9oVGuCK0sX3RD2E1AhfDOHXj4kZv807ssyyK4L05Jgs+O6yQCXdx2COaW1P/lA3mGZg/sVOAN63z/udwYQ/lxQ/eDymyyuDhfHUk+zctnGk0ZAimwR8MAZ1KKHJ4GuD6zfdDnlvdcP/iXV+/ZnrtHDbZvn+PC9E8GUjsKIOKSeFxakH+fG9fbjOZGyUYy6RusVye1oo0exFKAV8CKDVP/ruVSXDYZfafmPBQunn4TUmnZQGFz5oxSiIcInXyoUgBmimLaKwdM+4wvxacTDz/svJ+4cVv8fh70uRDxHIpXD/HKoeLCyhkpNeoCMxxZk";


    private VuforiaLocalizer vuforia;

    String[] settings = {
            "",
            "Duck Freight",
            "Duck No Freight",
            "Warehouse Freight",
            "Warehouse No Freight",
            ""};

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_DM.tflite";
    private static final String[] LABELS = {
            //"Ball",
            //"Cube",
            "Duck",
            "Marker"
    };
    private TFObjectDetector tfod;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        int amountducks = 0;

        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            tfod.setZoom(1, 16.0/9.0);
        }

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFrontDrive = hardwareMap.get(DcMotor.class, "fl");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "fr");
        leftBackDrive = hardwareMap.get(DcMotor.class, "bl");
        rightBackDrive = hardwareMap.get(DcMotor.class, "br");
        robotArm = hardwareMap.get(DcMotor.class, "ra");
        intake = hardwareMap.get(DcMotor.class, "in");
        spinner = hardwareMap.get(DcMotor.class, "sc");

        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        spinner.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        robotArm.setDirection(DcMotor.Direction.REVERSE);

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotArm.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);

        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotArm.setTargetPosition(0);
        robotArm.setPower(1);
        robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addData("Please choose a mode!", "up - redDuck, right - redWarehouse, left - blueDuck, down - blueWarehouse");
        telemetry.update();

        while (!choosingAuto) {




            telemetry.addData("Please choose a setting", "");
            if (redOrBlue) {
                telemetry.addData("Red", settings[onSetting - 1]);
                telemetry.addData("Red", ">" + settings[onSetting]);
                telemetry.addData("Red", settings[onSetting + 1]);
            } else {
                telemetry.addData("Blue", settings[onSetting - 1]);
                telemetry.addData("Blue", ">" + settings[onSetting]);
                telemetry.addData("Blue", settings[onSetting + 1]);
            }

            telemetry.addData("hub", hubNum);

            if (gamepad1.dpad_up) {
                if (onSetting > 1 && !settingButtonDown1) {
                    onSetting--;
                }
                settingButtonDown1 = true;
            } else {
                settingButtonDown1 = false;
            }

            if (gamepad1.dpad_down) {
                if (onSetting < 4 && !settingButtonDown2) {
                    onSetting++;
                }
                settingButtonDown2 = true;
            } else {
                settingButtonDown2 = false;
            }

            if (gamepad1.dpad_left) {
                redOrBlue = true;
            } else if (gamepad1.dpad_right) {
                redOrBlue = false;
            }

            if (isStarted()) {
                if (redOrBlue) {
                    choosingAuto = true;
                    autoMode = onSetting;
                } else {
                    choosingAuto = true;
                    autoMode = onSetting + 4;
                }
            }
            hubNum = getElement();
            telemetry.update();



        }
/*
        telemetry.clearAll();
        telemetry.addData("DcMotors", "Starting at %7d :%7d :%7d :%7d",
                rightFrontDrive.getCurrentPosition(),
                leftFrontDrive.getCurrentPosition(),
                rightBackDrive.getCurrentPosition(),
                leftBackDrive.getCurrentPosition());
        telemetry.update();
*/
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        telemetry.addData("DcMotors", "Starting at %7d :%7d :%7d :%7d",
                rightFrontDrive.getCurrentPosition(),
                leftFrontDrive.getCurrentPosition(),
                rightBackDrive.getCurrentPosition(),
                leftBackDrive.getCurrentPosition());
        //spinner.getCurrentPosition());
        telemetry.update();

        //enter autonomous code here//////////////////////////////////////////////////////////////////////////////////////////
        switch (autoMode) {
            case 1:
                redDuckWithFreight();
                break;
            case 2:
                redDuckWithNoFreight();
                break;
            case 3:
                redWarehouseWithFreight();
                break;
            case 4:
                redWarehouseWithNoFreight();
                break;
            case 5:
                blueDuckWithFreight();
                //flipHub();
                break;
            case 6:
                blueDuckWithNoFreight();
                //flipHub();
                break;
            case 7:
                blueWarehouseWithFreight();
                //flipHub();
                break;
            case 8:
                blueWarehouseWithNoFreight();
                //flipHub();
                break;
        }

        while(opModeIsActive());
    }

    public void redDuckWithFreight() {



        moveRight(13, MEDIUM);
        waitForDriveMotorsFast();
        moveForward(-4.5, MEDIUM);
        waitForDriveMotorsFast();
        moveRight(4.5, 0.1);
        waitForDriveMotorsFast();
        spinnerRed(SPINNER_SPEED);
        sleep(SPIN_DURATION - 500);
        spinnerEnd();
        moveForward(-8, MEDIUM);
        waitForDriveMotorsFast();
        moveRight(8, 0.1);
        waitForDriveMotorsFast();
        moveForward(-16, MEDIUM);
        waitForDriveMotorsFast();
        moveRight(-8, 0.1);
        waitForDriveMotorsFast();
        rotateRight(-90, MEDIUM);
        waitForDriveMotors();

        switch(hubNum){//30 towards hub
            case 1:
                level1(30);
                break;
            case 2:
                level2(30);
                break;
            case 3:
                level3(30);
                break;
        }




        /*
        moveRight(-8, SLOW);
        waitForDriveMotors();
        moveForward(7, SLOW);
        waitForDriveMotors();
        moveForward(-35, SLOW);//
        waitForDriveMotors();
        rotateRight(-90, SLOW);
        waitForDriveMotors();
        switch(hubNum){
            case 1:
                level1();
                break;
            case 2:
                level2();
                break;
            case 3:
                level3();
                break;
        }
        moveForward(-25, SLOW);
        waitForDriveMotors();
        rotateRight(90, SLOW);
        waitForDriveMotors();
        moveForward(20, SLOW);
        waitForDriveMotors();
        moveRight(5, SLOW);

         */
    }

    public void redDuckWithNoFreight() {
        moveRight(13, SLOW);
        waitForDriveMotors();
        rotateRight(-90, SLOW);
        waitForDriveMotors();
        moveForward(4.5, SLOW);
        waitForDriveMotors();
        moveRight(5, 0.1);
        waitForDriveMotors();
        spinnerRed(SPINNER_SPEED);
        sleep(SPIN_DURATION);
        spinnerEnd();
        moveBackward(21, SLOW);
        waitForDriveMotors();
    }

    public void redWarehouseWithFreight() {
        //positioning to score
        moveRight(18,MEDIUM);
        waitForDriveMotors();
        moveBackward(7,MEDIUM);
        waitForDriveMotors();
        rotateRight(180, MEDIUM);
        waitForDriveMotors();
        moveForward(16, MEDIUM);
        waitForDriveMotors();
        //scoring
        liftArm(LIFT_5,LIFT_ARM_ROTATE_PWR);
        waitForArm();
        takeOut(1);
        sleep(500);
        intakeOff();
        liftArm(LIFT_0,LIFT_ARM_ROTATE_PWR);
        waitForArm();
        //parking in the warehouse
        moveRight(17, MEDIUM);
        waitForDriveMotors();
        rotateRight( -80, MEDIUM);
        waitForDriveMotors();
        moveLeft(32, SLOW);
        waitForDriveMotorsFast();
        moveBackward(41, MEDIUM);
        waitForDriveMotors();
        moveRight(26, MEDIUM);
        waitForDriveMotors();
    }

    public void redWarehouseWithNoFreight() {
//        liftArm(29, LIFT_ARM_ROTATE_PWR); // was 10
        moveBackward(36, SLOW);
        waitForDriveMotors();
        moveRight(25, SLOW);
        waitForDriveMotors();
        rotateRight(-90, SLOW);
        waitForDriveMotors();
        moveLeft(25, SLOW);
        waitForDriveMotors();
//        liftArm(-29, LIFT_ARM_ROTATE_PWR); // was -10
    }

    public void blueDuckWithFreight() { // unfinished
        moveRight(-4.75, SLOW);
        waitForDriveMotors();
        moveForward(5, SLOW);
        waitForDriveMotors();
        spinnerBlue(SPINNER_SPEED);
        sleep(SPIN_DURATION);
        spinnerEnd();
        moveForward(-5, SLOW);
        waitForDriveMotors();
        moveRight(6, SLOW);
        waitForDriveMotorsFast();
        moveForward(-33, SLOW);
        waitForDriveMotors();
        moveRight(-20, SLOW);
        waitForDriveMotors();
        rotateRight(-90, SLOW);
        waitForDriveMotors();
        moveRight(-10, SLOW / 2);
        waitForDriveMotors();
        moveForward(7, SLOW / 2);
        waitForDriveMotors();
        liftArm(LIFT_3, LIFT_ARM_ROTATE_PWR);
        sleep(3000);
        liftArm(-LIFT_3, LIFT_ARM_ROTATE_PWR);
        sleep(3000);
        moveRight(60, SLOW);
        waitForDriveMotors();
    }

    public void blueDuckWithNoFreight() {
        moveRight(-4.75, SLOW);
        waitForDriveMotors();
        moveForward(2.75, SLOW);
        waitForDriveMotors();
        spinnerBlue(SPINNER_SPEED);
        sleep(SPIN_DURATION);
        spinnerEnd();
        moveForward(-6.5, SLOW);
        waitForDriveMotors();
        moveRight(6, SLOW);
        waitForDriveMotors();
        moveRight(-31, SLOW);
        waitForDriveMotors();
        moveForward(18, SLOW);
    }

    public void blueWarehouseWithFreight() {
        //positioning to score
        moveLeft(18, MEDIUM);
        waitForDriveMotors();
        moveBackward(7, MEDIUM);
        waitForDriveMotors();
        rotateRight(180, SLOW);
        waitForDriveMotors();
        moveForward(16, MEDIUM);
        waitForDriveMotors();
        //scoring
        liftArm(LIFT_5, LIFT_ARM_ROTATE_PWR);
        waitForArm();
        takeOut(1);
        sleep(500);
        intakeOff();
        liftArm(LIFT_0, LIFT_ARM_ROTATE_PWR);
        waitForArm();
        //parking in the warehouse
        moveLeft(19, MEDIUM);
        waitForDriveMotors();
        rotateRight(90, SLOW);
        waitForDriveMotors();
        moveRight(28, MEDIUM);
        waitForDriveMotorsFast();
        moveBackward(36, MEDIUM);
        waitForDriveMotors();
        moveLeft(26, MEDIUM);
        waitForDriveMotors();
    }

    public void blueWarehouseWithNoFreight() {
//        liftArm(29, LIFT_ARM_ROTATE_PWR); // was 10
//        waitForDriveMotors();
        moveForward(-36, SLOW);
        waitForDriveMotors();
        moveRight(-25, SLOW);
        waitForDriveMotors();
        rotateRight(90, SLOW);
        waitForDriveMotors();
        moveRight(25, SLOW);
        waitForDriveMotors();
//        liftArm(-29, LIFT_ARM_ROTATE_PWR); // was -10
    }


    public void level1(double inches){
        moveForward(inches, SLOW);
        waitForDriveMotors();
        liftArm(459, LIFT_ARM_ROTATE_PWR); // was 160
        sleep(1500);
        takeOut(1);
        sleep(3000);
        intakeOff();
        liftArm(-115, LIFT_ARM_ROTATE_PWR); // was -40
        sleep(1500);
    }

    public void level2(double inches){
        level1(inches);
    }

    public void level3(double inches){
        level1(inches);
    }



    /*public void flipHub(){
        if(hubNum == 3){
            hubNum = 1;
        }else{
            if(hubNum == 1){
                hubNum = 3;
            }
        }
    }*/

    public int inchestoticks(double inches) {
        return (int) Math.round((inches * ticksperrotation) / (mmperin * wheeldiameter * pi));
    }

    public void moveBackward(double inches, double speed) {
        moveForward(-inches, speed);
    }

    public void moveForward(double inches, double speed) {
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setTargetPosition(inchestoticks(48*inches/46));
        rightFrontDrive.setTargetPosition(inchestoticks(48*inches/46));
        leftBackDrive.setTargetPosition(inchestoticks(48*inches/46));
        rightBackDrive.setTargetPosition(inchestoticks(48*inches/46));

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);
    }

    public void liftArm(int ticks, double power) {
        robotArm.setTargetPosition(ticks);
        robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotArm.setPower(power);
    }

    public void rotateRight(int degrees, double speed) {
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setTargetPosition(degrees * 36/5);
        rightFrontDrive.setTargetPosition(-degrees * 36/5);
        leftBackDrive.setTargetPosition(degrees * 36/5);
        rightBackDrive.setTargetPosition(-degrees * 36/5);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);
    }

    public void moveDiagonal(double inches, double speed) { //WORK IN PROGRESS
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //leftFrontDrive.setTargetPosition(inchestoticks(inches));
        rightFrontDrive.setTargetPosition(-inchestoticks(inches));
        leftBackDrive.setTargetPosition(-inchestoticks(inches));
        //rightBackDrive.setTargetPosition(inchestoticks(inches));

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);
    }
    public void moveLeft(double inches, double speed) {
        moveRight(-inches, speed);
    }
    public void moveRight(double inches, double speed) {
        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setTargetPosition(inchestoticks((48*48)*inches/(42*46.5)));
        rightFrontDrive.setTargetPosition(-inchestoticks((48*48)*inches/(42*46.5)));
        leftBackDrive.setTargetPosition(-inchestoticks((48*48)*inches/(42*46.5)));
        rightBackDrive.setTargetPosition(inchestoticks((48*48)*inches/(42*46.5)));

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);
    }

    public void takeIn(double speed) {
        intake.setPower(-speed);
    }

    public void takeOut(double speed) {
        intake.setPower(speed);
    }

    void intakeOff() {
        intake.setPower(0);
    }

    public void waitForDriveMotors() {
        while (leftFrontDrive.isBusy() || rightFrontDrive.isBusy() || leftBackDrive.isBusy() || rightBackDrive.isBusy()) {
            telemetry.update();
        }
        
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    public void waitForDriveMotorsFast() {
        while (leftFrontDrive.isBusy() && rightFrontDrive.isBusy() && leftBackDrive.isBusy() && rightBackDrive.isBusy()) {
            telemetry.update();
        }
        
        leftFrontDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightBackDrive.setPower(0);
    }

    public void waitForArm() {
        while (robotArm.isBusy())
        {
            telemetry.update();
        }


    }

    public void spinnerBlue(double speed) {
        spinner.setPower(-speed);
    }

    public void spinnerRed(double speed) {
        spinner.setPower(speed);
    }

    public void spinnerEnd() {
        spinner.setPower(0);
    }

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    public int getElement(){

        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
               // telemetry.addData("# Object Detected", updatedRecognitions.size());
                // step through the list of recognitions and display boundary info.
                int i = 0;
                for (int p = 0; p != updatedRecognitions.size(); p++) {
                        /*telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                recognition.getLeft(), recognition.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                recognition.getRight(), recognition.getBottom());*/

                    if(updatedRecognitions.get(i).getLabel() != "Marker") {
                        telemetry.addData("", updatedRecognitions.get(i).getLeft());
                    }
                    if(updatedRecognitions.get(i).getLeft() < 180){
                        return 1;
                    }else{
                        return 2;
                    }


                }
                if(updatedRecognitions.size() == 0){
                    return 3;
                }


            }

        }
        return hubNum;
    }




}