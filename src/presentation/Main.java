package presentation;

import domain.member.MemberRepository;
import domain.payment.PaymentRepository;
import domain.plan.PlanRepository;
import domain.statistics.StatisticsRepository;
import domain.subscription.SubscriptionRepository;

import infrastructure.repository.MySqlMemberRepository;
import infrastructure.repository.MySqlPaymentRepository;
import infrastructure.repository.MySqlPlanRepository;
import infrastructure.repository.MySqlStatisticsRepository;
import infrastructure.repository.MySqlSubscriptionRepository;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner =
            new Scanner(System.in);

    // =========================
    // REPOSITORIES
    // =========================

    private static final MemberRepository memberRepository =
            new MySqlMemberRepository();

    private static final PaymentRepository paymentRepository =
            new MySqlPaymentRepository();

    private static final PlanRepository planRepository =
            new MySqlPlanRepository();

    private static final SubscriptionRepository subscriptionRepository =
            new MySqlSubscriptionRepository();

    private static final StatisticsRepository statisticsRepository =
            new MySqlStatisticsRepository();


    public static void main(String[] args) {

        while (true) {

            printMenu();

            int choice = readInt("Choose: ");

            try {

                switch (choice) {

                    case 1 -> registerMember();

                    case 2 -> createSubscription();

                    case 3 -> recordPayment();

                    case 4 -> renewSubscription();

                    case 5 -> showMembers();

                    case 6 -> showPlans();

                    case 7 -> createPlan();

                    case 8 -> updatePlanPrice();

                    case 9 -> deactivatePlan();

                    case 10 -> showDashboard();

                    case 11 -> showInactiveMembers();

                    case 12 -> expireSubscriptions();

                    case 13 -> printReceipt();

                    case 0 -> {
                        System.out.println("Goodbye!");
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Invalid choice."
                            );
                }

            } catch (RuntimeException e) {

                System.out.println();
                System.out.println(
                        "ERROR: " + e.getMessage()
                );
            }
        }
    }


    // =========================
    // MENU
    // =========================

    private static void printMenu() {

        System.out.println();
        System.out.println(
                "=========================================="
        );
        System.out.println(
                "        GYM MANAGEMENT SYSTEM"
        );
        System.out.println(
                "=========================================="
        );

        System.out.println("1.  Register Member");
        System.out.println("2.  New Subscription");
        System.out.println("3.  Record Payment");
        System.out.println("4.  Renew Subscription");
        System.out.println("5.  Show Members");
        System.out.println("6.  Show Plans");
        System.out.println("7.  Create Plan");
        System.out.println("8.  Update Plan Price");
        System.out.println("9.  Deactivate Plan");
        System.out.println("10. Dashboard");
        System.out.println("11. Inactive Members");
        System.out.println("12. Expire Subscriptions");
        System.out.println("13. Print Receipt");

        System.out.println("0.  Exit");

        System.out.println(
                "=========================================="
        );
    }


    // =========================
    // MEMBER
    // =========================

    private static void registerMember() {

        System.out.println(
                "Register Member - connect existing service"
        );
    }


    private static void showMembers() {

        System.out.println(
                "Show Members - connect existing service"
        );
    }


    private static void showInactiveMembers() {

        System.out.println(
                "Inactive Members - connect existing service"
        );
    }


    // =========================
    // SUBSCRIPTION
    // =========================

    private static void createSubscription() {

        System.out.println(
                "New Subscription - connect existing service"
        );
    }


    private static void renewSubscription() {

        System.out.println(
                "Renew Subscription - connect existing service"
        );
    }


    private static void expireSubscriptions() {

        System.out.println(
                "Expire Subscriptions - connect existing service"
        );
    }


    // =========================
    // PAYMENT
    // =========================

    private static void recordPayment() {

        System.out.println(
                "Record Payment - connect existing service"
        );
    }


    // =========================
    // PLAN
    // =========================

    private static void showPlans() {

        System.out.println(
                "Show Plans - connect existing service"
        );
    }


    private static void createPlan() {

        System.out.println(
                "Create Plan - connect existing service"
        );
    }


    private static void updatePlanPrice() {

        System.out.println(
                "Update Plan Price - connect existing service"
        );
    }


    private static void deactivatePlan() {

        System.out.println(
                "Deactivate Plan - connect existing service"
        );
    }


    // =========================
    // DASHBOARD
    // =========================

    private static void showDashboard() {

        System.out.println(
                "Dashboard - connect existing service"
        );
    }


    // =========================
    // RECEIPT
    // =========================

    private static void printReceipt() {

        System.out.println(
                "Receipt - connect existing service"
        );
    }


    // =========================
    // INPUT
    // =========================

    private static int readInt(String message) {

        System.out.print(message);

        return Integer.parseInt(
                scanner.nextLine()
        );
    }
}