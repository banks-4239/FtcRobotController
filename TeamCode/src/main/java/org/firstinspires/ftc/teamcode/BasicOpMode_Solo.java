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

//here ya go DAVID i did it i did the thing i forgor :skull: what i did xd but it sure was something :D

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
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

@TeleOp(name = "Basic: Linear Solo OpMode", group = "Linear Solo Opmode")
//@Disabled
public class BasicOpMode_Solo extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor robotArm = null;
    private DcMotor intake = null;
    private DcMotor spinner = null;

    public Gamepad activeGamepad1 = null;
    public Gamepad activeGamepad2 = null;

    private DigitalChannel armButton;

    double mmperin = 0.039701;

    int switchingConts = 0;

    double ticksperrotation = 537.7;
    double ticksperdegree = 3.95861111111;
    double wheeldiameter = 100;
    double pi = 3.1415;

    // 2.87
    static int LIFT_5 = 5100; // 1850;
    static int LIFT_4 = 3444; // 1200;
    static int LIFT_3 = 1578; // 550;
    static int LIFT_2 = 947;  // 330;
    static int LIFT_1 = 344;  // 120;
    static int LIFT_0 = 00;

    static final double LIFT_ARM_ROTATE_PWR = 1;

    @Override
    public void runOpMode() {
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
        armButton = hardwareMap.get(DigitalChannel.class, "ad");

        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        spinner.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);
        robotArm.setDirection(DcMotor.Direction.REVERSE);

        armButton.setMode(DigitalChannel.Mode.INPUT);

        boolean facingFront = true;
        int toggled = 0;

        robotArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int whereArm = 0;
        int ArmMoveTo = 0;

        boolean readyToCap = false;

        boolean cutscene = false;



        activeGamepad1 = gamepad1;
        activeGamepad2 = gamepad2;

        robotArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Setup a variable for each drive wheel to save power level for telemetry
            double moveX = activeGamepad1.left_stick_x;
            double moveY = activeGamepad1.left_stick_y;

            boolean spinLeft = gamepad1.left_bumper;
            boolean spinRight = gamepad1.right_bumper;

            boolean toggleButton = activeGamepad1.x;

            boolean lift0 = gamepad1.dpad_down;
            boolean lift1 = gamepad1.dpad_left;
            boolean lift2 = gamepad1.dpad_right;
            boolean lift3 = gamepad1.dpad_up;
            boolean lift4 = gamepad1.back;
            boolean lift5 = gamepad1.start;

            double rotate = activeGamepad1.right_stick_x;

            boolean fast = activeGamepad1.right_stick_button;

            boolean takingIn = activeGamepad1.a;
            boolean takingOut = activeGamepad1.b;

            double armUp = activeGamepad1.right_trigger;
            double armDown = activeGamepad1.left_trigger;

            boolean armIsDown = armButton.getState();

            boolean armMoving;

            if (!cutscene) {
                if (activeGamepad1.back) {
                    cutscene = true;
                }

                if (facingFront) {
                    if (fast) {
                        leftFrontDrive.setPower(moveY + rotate - moveX);
                        rightFrontDrive.setPower(moveY - rotate + moveX);
                        rightBackDrive.setPower(moveY - rotate - moveX);
                        leftBackDrive.setPower(moveY + rotate + moveX);
                    } else {
                        leftFrontDrive.setPower(((moveY / 2) + (rotate / 1.5) - (moveX / 2)));
                        rightFrontDrive.setPower(((moveY / 2) - (rotate / 1.5) + (moveX / 2)));
                        rightBackDrive.setPower(((moveY / 2) - (rotate / 1.5) - (moveX / 2)));
                        leftBackDrive.setPower(((moveY / 2) + (rotate / 1.5) + (moveX / 2)));
                    }
                } else {
                    if (fast) {
                        leftFrontDrive.setPower(-moveY + rotate + moveX);
                        rightFrontDrive.setPower(-moveY - rotate - moveX);
                        rightBackDrive.setPower(-moveY - rotate + moveX);
                        leftBackDrive.setPower(-moveY + rotate - moveX);
                    } else {
                        leftFrontDrive.setPower((-(moveY / 2) + (rotate / 1.5) + (moveX / 2)));
                        rightFrontDrive.setPower((-(moveY / 2) - (rotate / 1.5) - (moveX / 2)));
                        rightBackDrive.setPower((-(moveY / 2) - (rotate / 1.5) + (moveX / 2)));
                        leftBackDrive.setPower((-(moveY / 2) + (rotate / 1.5    ) - (moveX / 2)));
                    }
                }

                if (lift0) {
                    robotArm.setTargetPosition(LIFT_0);
                    robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robotArm.setPower(LIFT_ARM_ROTATE_PWR);
                }

                if (lift1) {
                    robotArm.setTargetPosition(LIFT_1);
                    robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robotArm.setPower(LIFT_ARM_ROTATE_PWR);
                }

                if (lift2) {
                    robotArm.setTargetPosition(LIFT_2);
                    robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robotArm.setPower(LIFT_ARM_ROTATE_PWR);
                }

                if (lift3) {
                    robotArm.setTargetPosition(LIFT_3);
                    robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robotArm.setPower(LIFT_ARM_ROTATE_PWR);
                }

                if (lift4) {
                    robotArm.setTargetPosition(LIFT_4);
                    robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robotArm.setPower(LIFT_ARM_ROTATE_PWR);

                }

                if (lift5) {
                    robotArm.setTargetPosition(LIFT_5);
                    robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robotArm.setPower(LIFT_ARM_ROTATE_PWR);

                }

                if (gamepad1.y && robotArm.getCurrentPosition() > 3157) { // 1100) {
                    readyToCap = false;
                    robotArm.setTargetPosition(LIFT_5);
                    robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    robotArm.setPower(0.5);
                }

                if (activeGamepad1.start) {
                    rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }

                if (toggleButton == true && toggled == 0) {
                    toggled = 1;
                    facingFront = false;
                }

                if (toggleButton == false && toggled == 1) {
                    toggled = 2;
                }

                if (toggleButton == true && toggled == 2) {
                    toggled = 3;
                    facingFront = true;
                }

                if (toggleButton == false && toggled == 3) {
                    toggled = 0;
                }

                if (takingIn) {
                    intake.setPower(1);
                }

                if (takingOut) {
                    intake.setPower(-1);
                }

                if ((takingOut == false) && (takingIn == false)) {
                    intake.setPower(0);
                }

                /*
                cap:1940
                pickup:50
                layer 3:2130
                layer 2: 590
                layer 1: 330
                */

                // telemetry.addData("FRONT LEFT", leftFrontDrive.getCurrentPosition());
                // telemetry.addData("FRONT RIGHT", rightFrontDrive.getCurrentPosition());
                // telemetry.addData("BACK LEFT", leftBackDrive.getCurrentPosition());
                // telemetry.addData("BACK RIGHT", rightBackDrive.getCurrentPosition());
                telemetry.addData("armPosition", robotArm.getCurrentPosition());

                if (spinLeft) {
                    spinner.setPower(0.5);
                } else {
                    if (spinRight) {
                        spinner.setPower(-0.5);
                    } else {
                        spinner.setPower(0);
                    }
                }
                /* on comment indefinetly
                if (armUp > 0.3) {
                    robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robotArm.setPower(0.5);
                    whereArm = robotArm.getCurrentPosition();
                } else {
                    if (armDown > 0.3 && robotArm.getCurrentPosition() > 50) {
                        robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robotArm.setPower(-0.5);
                        whereArm = robotArm.getCurrentPosition();
                    } else {
                        robotArm.setPower(1);
                        robotArm.setTargetPosition(whereArm);
                        robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    }
                }*/


            } else {
                //enter coding here
                telemetry.update();

                moveWheels(0, 1800, 1500, 100, 1);
                moveWheels(500, 500, 500, 500, 1);
                moveWheels(-1900, 0, 100, -1600, 1);

                //stop coding here
                leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                cutscene = false;
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            // telemetry.addData("", toggled);
            // telemetry.addData("Spinner", "left (%.2f)", activeGamepad1.right_trigger);
            telemetry.update();
        }
    }


    void moveWheels(int FL, int FR, int BR, int BL, double speed) {
        int max = absMax(absMax(FL, FR), absMax(BL, BR));

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontDrive.setTargetPosition(FL);
        rightFrontDrive.setTargetPosition(FR);
        leftBackDrive.setTargetPosition(BL);
        rightBackDrive.setTargetPosition(BR);

        leftFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftFrontDrive.setPower(speed);
        rightFrontDrive.setPower(speed);
        leftBackDrive.setPower(speed);
        rightBackDrive.setPower(speed);

        sleep(3000);
    }

    int absMax(int a, int b) {
        return (Math.max(Math.abs(a), Math.abs(b)));
    }
}