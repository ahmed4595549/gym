package presentation;

import application.member.*;
import application.payment.RecordPaymentService;
import application.plan.*;
import application.receipt.GetPaymentReceiptService;
import application.receipt.Receipt;
import application.statistics.DashboardStatistics;
import application.statistics.GetDashboardStatisticsService;
import application.subscription.ExpireSubscriptionsService;
import application.subscription.SubscribeMemberService;

import domain.member.*;
import domain.payment.*;
import domain.plan.*;
import domain.subscription.*;

import infrastructure.repository.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GymFrame extends JFrame {

    private final MemberRepository memberRepository =
            new MySqlMemberRepository();

    private final PaymentRepository paymentRepository =
            new MySqlPaymentRepository();

    private final PlanRepository planRepository =
            new MySqlPlanRepository();

    private final SubscriptionRepository subscriptionRepository =
            new MySqlSubscriptionRepository();

    private final RegisterMemberService registerMemberService =
            new RegisterMemberService(memberRepository);

    private final UpdateMemberService updateMemberService =
            new UpdateMemberService(memberRepository);

    private final ActivateMemberService activateMemberService =
            new ActivateMemberService(memberRepository);

    private final DeactivateMemberService deactivateMemberService =
            new DeactivateMemberService(memberRepository);

    private final CreatePlanService createPlanService =
            new CreatePlanService(planRepository);

    private final GetPlansService getPlansService =
            new GetPlansService(planRepository);

    private final UpdatePlanPriceService updatePlanPriceService =
            new UpdatePlanPriceService(planRepository);

    private final DeactivatePlanService deactivatePlanService =
            new DeactivatePlanService(planRepository);

    private final SubscribeMemberService subscribeMemberService =
            new SubscribeMemberService(
                    memberRepository,
                    planRepository,
                    subscriptionRepository
            );

    private final ExpireSubscriptionsService expireSubscriptionsService =
            new ExpireSubscriptionsService(subscriptionRepository);

    private final RecordPaymentService recordPaymentService =
            new RecordPaymentService(
                    paymentRepository,
                    subscriptionRepository
            );

    private final GetPaymentReceiptService receiptService =
            new GetPaymentReceiptService(
                    paymentRepository,
                    subscriptionRepository
            );

    private final GetDashboardStatisticsService statisticsService =
            new GetDashboardStatisticsService(
                    new MySqlStatisticsRepository()
            );

    private final DefaultTableModel membersModel =
            new DefaultTableModel(
                    new String[]{
                            "ID",
                            "Name",
                            "Phone",
                            "Registration Date",
                            "Status"
                    },
                    0
            ) {
                @Override
                public boolean isCellEditable(
                        int row,
                        int column
                ) {
                    return false;
                }
            };

    private final DefaultTableModel plansModel =
            new DefaultTableModel(
                    new String[]{
                            "ID",
                            "Name",
                            "Price",
                            "Duration",
                            "Access",
                            "Status"
                    },
                    0
            ) {
                @Override
                public boolean isCellEditable(
                        int row,
                        int column
                ) {
                    return false;
                }
            };

    private final DefaultTableModel subscriptionsModel =
            new DefaultTableModel(
                    new String[]{
                            "ID",
                            "Member",
                            "Plan",
                            "Price",
                            "Start",
                            "End",
                            "Status"
                    },
                    0
            ) {
                @Override
                public boolean isCellEditable(
                        int row,
                        int column
                ) {
                    return false;
                }
            };

    private final JTextArea dashboardArea =
            new JTextArea();

    public GymFrame() {

        setTitle("Gym Management System");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(1100, 700);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel title =
                new JLabel(
                        "GYM MANAGEMENT SYSTEM",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        24
                )
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        10,
                        15,
                        10
                )
        );

        add(title, BorderLayout.NORTH);

        JTabbedPane tabs =
                new JTabbedPane();

        tabs.addTab(
                "Dashboard",
                buildDashboardTab()
        );

        tabs.addTab(
                "Members",
                buildMembersTab()
        );

        tabs.addTab(
                "Plans",
                buildPlansTab()
        );

        tabs.addTab(
                "Subscriptions",
                buildSubscriptionsTab()
        );

        tabs.addTab(
                "Payments",
                buildPaymentsTab()
        );

        tabs.addChangeListener(e -> {

            int index =
                    tabs.getSelectedIndex();

            if (index == 0)
                run(this::refreshDashboard);

            if (index == 1)
                run(this::refreshMembers);

            if (index == 2)
                run(this::refreshPlans);

            if (index == 3)
                run(this::refreshSubscriptions);
        });

        add(
                tabs,
                BorderLayout.CENTER
        );

        refreshDashboard();
    }

    private JPanel buildDashboardTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        dashboardArea.setEditable(false);

        dashboardArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        16
                )
        );

        dashboardArea.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        panel.add(
                new JScrollPane(
                        dashboardArea
                ),
                BorderLayout.CENTER
        );

        JButton refresh =
                new JButton(
                        "Refresh Dashboard"
                );

        refresh.addActionListener(
                e -> run(
                        this::refreshDashboard
                )
        );

        panel.add(
                refresh,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel buildMembersTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        JTable table =
                new JTable(
                        membersModel
                );

        panel.add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JButton register =
                new JButton(
                        "Register Member"
                );

        JButton activate =
                new JButton(
                        "Activate"
                );

        JButton deactivate =
                new JButton(
                        "Deactivate"
                );

        JButton update =
                new JButton(
                        "Update"
                );

        JButton refresh =
                new JButton(
                        "Refresh"
                );

        register.addActionListener(
                e -> run(
                        this::registerMember
                )
        );

        activate.addActionListener(
                e -> run(
                        () -> changeMemberStatus(true)
                )
        );

        deactivate.addActionListener(
                e -> run(
                        () -> changeMemberStatus(false)
                )
        );

        update.addActionListener(
                e -> run(
                        this::updateMember
                )
        );

        refresh.addActionListener(
                e -> run(
                        this::refreshMembers
                )
        );

        buttons.add(register);
        buttons.add(activate);
        buttons.add(deactivate);
        buttons.add(update);
        buttons.add(refresh);

        panel.add(
                buttons,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel buildPlansTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        JTable table =
                new JTable(
                        plansModel
                );

        panel.add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JButton create =
                new JButton(
                        "Create Plan"
                );

        JButton update =
                new JButton(
                        "Update Price"
                );

        JButton deactivate =
                new JButton(
                        "Deactivate"
                );

        JButton refresh =
                new JButton(
                        "Refresh"
                );

        create.addActionListener(
                e -> run(
                        this::createPlan
                )
        );

        update.addActionListener(
                e -> run(
                        this::updatePlanPrice
                )
        );

        deactivate.addActionListener(
                e -> run(
                        this::deactivatePlan
                )
        );

        refresh.addActionListener(
                e -> run(
                        this::refreshPlans
                )
        );

        buttons.add(create);
        buttons.add(update);
        buttons.add(deactivate);
        buttons.add(refresh);

        panel.add(
                buttons,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel buildSubscriptionsTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        JTable table =
                new JTable(
                        subscriptionsModel
                );

        panel.add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        JButton subscribe =
                new JButton(
                        "New Subscription"
                );

        JButton expire =
                new JButton(
                        "Expire Subscription"
                );

        JButton refresh =
                new JButton(
                        "Refresh"
                );

        subscribe.addActionListener(
                e -> run(
                        this::subscribe
                )
        );

        expire.addActionListener(
                e -> run(
                        this::expireSubscription
                )
        );

        refresh.addActionListener(
                e -> run(
                        this::refreshSubscriptions
                )
        );

        buttons.add(subscribe);
        buttons.add(expire);
        buttons.add(refresh);

        panel.add(
                buttons,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel buildPaymentsTab() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        JLabel info =
                new JLabel(
                        "Payment is separate from member registration.",
                        SwingConstants.CENTER
                );

        panel.add(
                info,
                BorderLayout.NORTH
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                20
                        )
                );

        JButton pay =
                new JButton(
                        "Record Payment"
                );

        JButton receipt =
                new JButton(
                        "Get Receipt"
                );

        pay.addActionListener(
                e -> run(
                        this::recordPayment
                )
        );

        receipt.addActionListener(
                e -> run(
                        this::showReceipt
                )
        );

        buttons.add(pay);
        buttons.add(receipt);

        panel.add(
                buttons,
                BorderLayout.CENTER
        );

        return panel;
    }

    private void registerMember() {

        String name =
                input("Member name:");

        String phone =
                input("Phone:");

        Member member =
                registerMemberService.register(
                        name,
                        phone
                );

        info(
                "Member registered successfully.\nID = "
                        + member.getId()
        );

        refreshMembers();
    }

    private void updateMember() {

        Integer id =
                intInput("Member ID:");

        String[] options = {
                "Name",
                "Phone"
        };

        String type =
                (String) JOptionPane.showInputDialog(
                        this,
                        "What do you want to update?",
                        "Update Member",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

        if (type == null)
            return;

        if (type.equals("Name")) {

            updateMemberService.updateName(
                    id,
                    input("New name:")
            );

        } else {

            updateMemberService.updatePhone(
                    id,
                    input("New phone:")
            );
        }

        info(
                "Member updated successfully."
        );

        refreshMembers();
    }

    private void changeMemberStatus(
            boolean activate
    ) {

        Integer id =
                intInput("Member ID:");

        if (activate) {

            activateMemberService.activate(id);

        } else {

            deactivateMemberService.deactivate(id);
        }

        info(
                "Member status updated."
        );

        refreshMembers();
    }

    private void createPlan() {

        String name =
                input("Plan name:");

        BigDecimal price =
                decimalInput("Price:");

        int duration =
                intInput(
                        "Duration in days:"
                );

        AccessType access =
                (AccessType)
                        JOptionPane.showInputDialog(
                                this,
                                "Access type:",
                                "Create Plan",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                AccessType.values(),
                                AccessType.ALL_DAYS
                        );

        if (access == null)
            return;

        Plan plan =
                createPlanService.create(
                        name,
                        price,
                        duration,
                        access
                );

        info(
                "Plan created successfully.\nID = "
                        + plan.getId()
        );

        refreshPlans();
    }

    private void updatePlanPrice() {

        Integer id =
                intInput("Plan ID:");

        BigDecimal price =
                decimalInput(
                        "New price:"
                );

        updatePlanPriceService.updatePrice(
                id,
                price
        );

        info(
                "Plan price updated."
        );

        refreshPlans();
    }

    private void deactivatePlan() {

        Integer id =
                intInput("Plan ID:");

        deactivatePlanService.deactivate(
                id
        );

        info(
                "Plan deactivated."
        );

        refreshPlans();
    }

    private void subscribe() {

        Integer memberId =
                intInput("Member ID:");

        Integer planId =
                intInput("Plan ID:");

        LocalDate startDate =
                LocalDate.parse(
                        input(
                                "Start date (YYYY-MM-DD):"
                        )
                );

        Subscription subscription =
                subscribeMemberService.subscribe(
                        memberId,
                        planId,
                        startDate
                );

        info(
                "Subscription created.\n"
                        + "ID = "
                        + subscription.getId()
                        + "\n\n"
                        + "Now record its payment."
        );

        refreshSubscriptions();
    }

    private void expireSubscription() {

        Integer id =
                intInput(
                        "Subscription ID:"
                );

        expireSubscriptionsService.expire(
                id
        );

        info(
                "Expire operation completed."
        );

        refreshSubscriptions();
    }

    private void recordPayment() {

        Integer subscriptionId =
                intInput(
                        "Subscription ID:"
                );

        BigDecimal amount =
                decimalInput(
                        "Original amount:"
                );

        BigDecimal discount =
                decimalInput(
                        "Discount:"
                );

        PaymentMethod method =
                (PaymentMethod)
                        JOptionPane.showInputDialog(
                                this,
                                "Payment method:",
                                "Record Payment",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                PaymentMethod.values(),
                                PaymentMethod.CASH
                        );

        if (method == null)
            return;

        Payment payment =
                recordPaymentService.pay(
                        subscriptionId,
                        amount,
                        discount,
                        method
                );

        info(
                "Payment recorded.\nID = "
                        + payment.getId()
        );
    }

    private void showReceipt() {

        Integer paymentId =
                intInput("Payment ID:");

        Receipt r =
                receiptService.get(
                        paymentId
                );

        String text =
                "PAYMENT RECEIPT\n\n"
                        + "Payment ID: "
                        + r.getPaymentId()
                        + "\n"
                        + "Member ID: "
                        + r.getMemberId()
                        + "\n"
                        + "Subscription ID: "
                        + r.getSubscriptionId()
                        + "\n"
                        + "Original Amount: "
                        + r.getOriginalAmount()
                        + "\n"
                        + "Discount: "
                        + r.getDiscount()
                        + "\n"
                        + "Paid Amount: "
                        + r.getPaidAmount()
                        + "\n"
                        + "Method: "
                        + r.getPaymentMethod()
                        + "\n"
                        + "Date: "
                        + r.getPaymentDate();

        JOptionPane.showMessageDialog(
                this,
                text,
                "Receipt",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void refreshMembers() {

        membersModel.setRowCount(0);

        List<Member> members =
                memberRepository.findAll();

        for (Member m : members) {

            membersModel.addRow(
                    new Object[]{
                            m.getId(),
                            m.getName(),
                            m.getPhone(),
                            m.getRegistrationDate(),
                            m.getStatus()
                    }
            );
        }
    }

    private void refreshPlans() {

        plansModel.setRowCount(0);

        for (Plan p :
                getPlansService.getAll()) {

            plansModel.addRow(
                    new Object[]{
                            p.getId(),
                            p.getName(),
                            p.getPrice(),
                            p.getDurationDays(),
                            p.getAccessType(),
                            p.getStatus()
                    }
            );
        }
    }

    private void refreshSubscriptions() {

        subscriptionsModel.setRowCount(0);

        List<Subscription> subscriptions =
                subscriptionRepository.findAll();

        for (Subscription s :
                subscriptions) {

            subscriptionsModel.addRow(
                    new Object[]{
                            s.getId(),
                            s.getMemberId(),
                            s.getPlanId(),
                            s.getAgreedPrice(),
                            s.getStartDate(),
                            s.getEndDate(),
                            s.getStatus()
                    }
            );
        }
    }

    private void refreshDashboard() {

        DashboardStatistics s =
                statisticsService.get();

        dashboardArea.setText(
                "GYM DASHBOARD\n"
                        + "==============================\n\n"
                        + "Total Members        : "
                        + s.getTotalMembers()
                        + "\n"
                        + "Active Members       : "
                        + s.getActiveMembers()
                        + "\n"
                        + "Expired Members      : "
                        + s.getExpiredMembers()
                        + "\n"
                        + "New This Month       : "
                        + s.getNewMembersThisMonth()
                        + "\n\n"
                        + "Payments Today       : "
                        + s.getPaymentsToday()
                        + "\n"
                        + "Payments This Month  : "
                        + s.getPaymentsThisMonth()
                        + "\n\n"
                        + "Active Subscriptions : "
                        + s.getActiveSubscriptions()
                        + "\n"
                        + "Expired Subscriptions: "
                        + s.getExpiredSubscriptions()
                        + "\n"
                        + "Expiring This Week   : "
                        + s.getExpiringThisWeek()
                        + "\n"
                        + "Renewals             : "
                        + s.getRenewals()
                        + "\n"
        );
    }

    private String input(
            String message
    ) {

        String value =
                JOptionPane.showInputDialog(
                        this,
                        message
                );

        if (value == null ||
                value.isBlank()) {

            throw new IllegalArgumentException(
                    "Input is required."
            );
        }

        return value.trim();
    }

    private Integer intInput(
            String message
    ) {

        try {

            return Integer.parseInt(
                    input(message)
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Please enter a valid integer."
            );
        }
    }

    private BigDecimal decimalInput(
            String message
    ) {

        try {

            return new BigDecimal(
                    input(message)
            );

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Please enter a valid number."
            );
        }
    }

    private void info(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void run(
            Runnable action
    ) {

        try {

            action.run();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage() == null
                            ? e.toString()
                            : e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}