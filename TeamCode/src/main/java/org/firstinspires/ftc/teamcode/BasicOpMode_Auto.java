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

    static int LIFT_3 = 5567; // 1940;
    static int LIFT_2 = 6113; // 2130;
    static int LIFT_1 = 947;  // 330;
    static int LIFT_0 = 0;    // 50;

    boolean settingButtonDown1 = false;
    boolean settingButtonDown2 = false;

    double ticksperrotation = 537.7;
    double ticksperdegree = 1.06805555556;
    double wheeldiameter = 100;
    double pi = 3.1415;
    
    static final double SPINNER_SPEED = .3;
    
    static final double LIFT_ARM_ROTATE_PWR = 1;

    static final double MAX_SPEED = 1 * .3;

    static final long SPIN_DURATION = 3500;

    boolean redOrBlue = true;

    String[] settings = {
            "",
            "Duck Freight",
            "Duck No Freight",
            "Warehouse Freight",
            "Warehouse No Freight",
            ""};

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

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
        robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

            telemetry.update();
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

            if (gamepad1.left_bumper) {
                redOrBlue = true;
            } else if (gamepad1.right_bumper) {
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
                break;
            case 6:
                blueDuckWithNoFreight();
                break;
            case 7:
                blueWarehouseWithFreight();
                break;
            case 8:
                blueWarehouseWithNoFreight();
                break;
        }

        while(opModeIsActive());
    }

    public void redDuckWithFreight() {
        moveForward(5, MAX_SPEED);
        waitForDriveMotors();
        moveRight(8, MAX_SPEED);
        waitForDriveMotors();
        spinnerRed(SPINNER_SPEED);
        sleep(SPIN_DURATION);
        spinnerEnd();
        moveRight(-8, MAX_SPEED);
        waitForDriveMotors();
        moveForward(7, MAX_SPEED);
        waitForDriveMotors();
        moveForward(-35, MAX_SPEED);//
        waitForDriveMotors();
        rotateRight(-90, MAX_SPEED);
        waitForDriveMotors();
        moveForward(25, MAX_SPEED);
        liftArm(459, LIFT_ARM_ROTATE_PWR); // was 160
        sleep(1500);
        takeOut(1);
        sleep(3000);
        intakeOff();
        liftArm(-115, LIFT_ARM_ROTATE_PWR); // was -40
        sleep(1500);
        moveForward(-25, MAX_SPEED);
        waitForDriveMotors();
        rotateRight(90, MAX_SPEED);
        waitForDriveMotors();
        moveForward(20, MAX_SPEED);
        waitForDriveMotors();
        moveRight(5, MAX_SPEED);
    }

    public void redDuckWithNoFreight() {
        moveForward(-5, MAX_SPEED);
        waitForDriveMotors();
        moveRight(8, MAX_SPEED);
        waitForDriveMotors();
        spinnerRed(SPINNER_SPEED);
        sleep(SPIN_DURATION);
        spinnerEnd();
        moveRight(-8, MAX_SPEED);
        waitForDriveMotors();
        moveForward(6, MAX_SPEED);
        waitForDriveMotors();
        moveForward(-24.5, MAX_SPEED);
        waitForDriveMotors();
        moveRight(11, MAX_SPEED);
    }

    public void redWarehouseWithFreight() {
        moveForward(7, MAX_SPEED);
        waitForDriveMotors();
        liftArm(401, LIFT_ARM_ROTATE_PWR); // 140
        sleep(5000);
        moveForward(10, MAX_SPEED);
        waitForDriveMotors();
        takeOut(1);
        sleep(1000);
        intakeOff();
        liftArm(-287, LIFT_ARM_ROTATE_PWR); // -100
        sleep(8000);
        rotateRight(90, MAX_SPEED);
        waitForDriveMotors();
        moveForward(65, MAX_SPEED);
        liftArm(-114, LIFT_ARM_ROTATE_PWR); // -40
        sleep(2000);
        waitForDriveMotors();
        rotateRight(180, MAX_SPEED / 2);
    }

    public void redWarehouseWithNoFreight() {
//        liftArm(29, LIFT_ARM_ROTATE_PWR); // was 10
        moveForward(-36, MAX_SPEED);
        waitForDriveMotors();
        moveRight(30, MAX_SPEED);
        waitForDriveMotors();
        rotateRight(-52, MAX_SPEED);
        waitForDriveMotors();
        moveRight(-33, MAX_SPEED);
        waitForDriveMotors();
//        liftArm(-29, LIFT_ARM_ROTATE_PWR); // was -10
    }

    public void blueDuckWithFreight() { // unfinished
        moveRight(-4.75, MAX_SPEED);
        waitForDriveMotors();
        moveForward(5, MAX_SPEED);
        waitForDriveMotors();
        spinnerBlue(SPINNER_SPEED);
        sleep(SPIN_DURATION);
        spinnerEnd();
        moveForward(-5, MAX_SPEED);
        waitForDriveMotors();
        moveRight(6, MAX_SPEED);
        waitForDriveMotorsFast();
        moveForward(-33, MAX_SPEED);
        waitForDriveMotors();
        moveRight(-20, MAX_SPEED);
        waitForDriveMotors();
        rotateRight(-90, MAX_SPEED);
        waitForDriveMotors();
        moveRight(-10, MAX_SPEED / 2);
        waitForDriveMotors();
        moveForward(7, MAX_SPEED / 2);
        waitForDriveMotors();
        liftArm(LIFT_3, LIFT_ARM_ROTATE_PWR);
        sleep(3000);
        liftArm(-LIFT_3, LIFT_ARM_ROTATE_PWR);
        sleep(3000);
        moveRight(60, MAX_SPEED);
        waitForDriveMotors();
    }

    public void blueDuckWithNoFreight() {
        moveRight(-4.75, MAX_SPEED);
        waitForDriveMotors();
        moveForward(2.75, MAX_SPEED);
        waitForDriveMotors();
        spinnerBlue(SPINNER_SPEED);
        sleep(SPIN_DURATION);
        spinnerEnd();
        moveForward(-6.5, MAX_SPEED);
        waitForDriveMotors();
        moveRight(6, MAX_SPEED);
        waitForDriveMotors();
        moveRight(-33, MAX_SPEED);
        waitForDriveMotors();
        moveForward(18, MAX_SPEED);
    }

    public void blueWarehouseWithFreight() {
        rotateRight(1800, MAX_SPEED);
    }

    public void blueWarehouseWithNoFreight() {
//        liftArm(29, LIFT_ARM_ROTATE_PWR); // was 10
//        waitForDriveMotors();
        moveForward(-36, MAX_SPEED);
        waitForDriveMotors();
        moveRight(-30, MAX_SPEED);
        waitForDriveMotors();
        rotateRight(52,MAX_SPEED);
        waitForDriveMotors();
        moveRight(33, MAX_SPEED);
        waitForDriveMotors();
//        liftArm(-29, LIFT_ARM_ROTATE_PWR); // was -10
    }


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
        robotArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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

        robotArm.setPower(0);
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
}