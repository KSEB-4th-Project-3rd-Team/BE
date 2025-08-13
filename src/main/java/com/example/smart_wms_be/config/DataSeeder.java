package com.example.smart_wms_be.config;

import com.example.smart_wms_be.domain.*;
import com.example.smart_wms_be.repository.CompanyRepository;
import com.example.smart_wms_be.repository.InOutOrderRepository;
import com.example.smart_wms_be.repository.InventoryRepository;
import com.example.smart_wms_be.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;
    private final CompanyRepository companyRepository;
    private final InOutOrderRepository inOutOrderRepository;

    public DataSeeder(ItemRepository itemRepository, InventoryRepository inventoryRepository, CompanyRepository companyRepository, InOutOrderRepository inOutOrderRepository) {
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        this.companyRepository = companyRepository;
        this.inOutOrderRepository = inOutOrderRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (itemRepository.count() == 0) {
            System.out.println("Seeding Items and Inventory data...");
            seedItemsAndInventory();
        }

        if (companyRepository.count() == 0) {
            System.out.println("Seeding Companies data...");
            seedCompanies();
        }

        if (inOutOrderRepository.count() == 0) {
            System.out.println("Seeding In/Out Orders data...");
            seedInOutOrders();
        }
        
        System.out.println("Data seeding check complete.");
    }

    private void seedItemsAndInventory() {
        List<Item> itemsToCreate = Arrays.asList(
            Item.builder().itemCode("HANKOOK-KINERGY-EX").itemName("Kinergy EX").itemGroup("한국타이어").spec("205/55R16").unit("14개/1세트").unitPriceIn(110000.0).unitPriceOut(90000.0).build(),
            Item.builder().itemCode("KUMHO-SOLUS-TA31").itemName("Solus TA31").itemGroup("금호타이어").spec("205/55R16").unit("14개/1세트").unitPriceIn(108000.0).unitPriceOut(88000.0).build(),
            Item.builder().itemCode("NEXEN-NBLUE-HD-PLUS").itemName("N’Blue HD Plus").itemGroup("넥센타이어").spec("205/55R16").unit("14개/1세트").unitPriceIn(105000.0).unitPriceOut(85000.0).build(),
            Item.builder().itemCode("HANKOOK-VENTUS-PRIME4").itemName("Ventus Prime4").itemGroup("한국타이어").spec("225/45R17").unit("14개/1세트").unitPriceIn(150000.0).unitPriceOut(125000.0).build(),
            Item.builder().itemCode("KUMHO-MAJESTY9-SOLUS").itemName("Majesty9 Solus").itemGroup("금호타이어").spec("225/45R17").unit("14개/1세트").unitPriceIn(145000.0).unitPriceOut(120000.0).build(),
            Item.builder().itemCode("NEXEN-NFERA-AU5").itemName("N’fera AU5").itemGroup("넥센타이어").spec("225/45R17").unit("14개/1세트").unitPriceIn(142000.0).unitPriceOut(118000.0).build(),
            Item.builder().itemCode("HANKOOK-VENTUS-AIR-S").itemName("Ventus Air S").itemGroup("한국타이어").spec("215/55R17").unit("14개/1세트").unitPriceIn(180000.0).unitPriceOut(145000.0).build(),
            Item.builder().itemCode("HANKOOK-OPTIMO-H426").itemName("Optimo H426").itemGroup("한국타이어").spec("205/55R16").unit("14개/1세트").unitPriceIn(120000.0).unitPriceOut(85200.0).build(),
            Item.builder().itemCode("KUMHO-KRUGEN-HP71").itemName("Krugen HP71").itemGroup("금호타이어").spec("235/55R19").unit("14개/1세트").unitPriceIn(142800.0).unitPriceOut(128000.0).build(),
            Item.builder().itemCode("NEXEN-NPRIZ-AH8").itemName("N’PRIZ AH8").itemGroup("넥센타이어").spec("215/55R17").unit("14개/1세트").unitPriceIn(110000.0).unitPriceOut(77500.0).build(),
            Item.builder().itemCode("NEXEN-IQ-SERIES").itemName("i.Q Series").itemGroup("넥센타이어").spec("175/50R15").unit("14개/1세트").unitPriceIn(95500.0).unitPriceOut(77910.0).build()
        );
        List<Item> savedItems = itemRepository.saveAll(itemsToCreate);

        for (Item item : savedItems) {
            Inventory inventory = Inventory.builder()
                    .item(item)
                    .quantity(0)
                    .lastUpdated(LocalDateTime.now())
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    private void seedCompanies() {
        List<Company> companiesToCreate = new ArrayList<>();
        // Suppliers
        for (int i = 1; i <= 15; i++) {
            companiesToCreate.add(Company.builder().companyName("매입처 " + i).companyCode("S" + String.format("%03d", i)).address("주소 " + i).contactPerson("담당자 " + i).contactEmail("supplier"+i+"@example.com").contactPhone("010-1111-"+String.format("%04d", i)).type(List.of("매입처")).build());
        }
        // Clients
        for (int i = 1; i <= 15; i++) {
            companiesToCreate.add(Company.builder().companyName("납품처 " + i).companyCode("C" + String.format("%03d", i)).address("주소 " + (i+15)).contactPerson("담당자 " + (i+15)).contactEmail("client"+i+"@example.com").contactPhone("010-2222-"+String.format("%04d", i)).type(List.of("납품처")).build());
        }
        companyRepository.saveAll(companiesToCreate);
    }

    private void seedInOutOrders() {
        List<Item> allItems = itemRepository.findAll();
        List<Company> allCompanies = companyRepository.findAll();
        List<Company> suppliers = allCompanies.stream().filter(c -> c.getType().contains("매입처")).collect(Collectors.toList());
        List<Company> clients = allCompanies.stream().filter(c -> c.getType().contains("납품처")).collect(Collectors.toList());
        
        if (allItems.isEmpty() || suppliers.isEmpty() || clients.isEmpty()) {
            System.out.println("Cannot seed In/Out Orders because item or company data is missing.");
            return;
        }

        Random random = new Random();
        LocalDate today = LocalDate.now();
        
        // Simulate historical data
        for (int i = 0; i < 280; i++) { // 280 completed orders
            LocalDate randomDate = today.minusDays(random.nextInt(358) + 8); // From 8 days ago to 1 year ago
            createOrder(random, allItems, suppliers, clients, randomDate, OrderStatus.COMPLETED);
        }

        // Simulate recent pending data
        for (int i = 0; i < 20; i++) { // 20 pending/scheduled orders
            LocalDate randomDate = today.minusDays(random.nextInt(7)); // Within the last week
            OrderStatus status = random.nextBoolean() ? OrderStatus.PENDING : OrderStatus.SCHEDULED;
            createOrder(random, allItems, suppliers, clients, randomDate, status);
        }
    }

    private void createOrder(Random random, List<Item> allItems, List<Company> suppliers, List<Company> clients, LocalDate date, OrderStatus status) {
        // This method does not handle stock levels, which is a simplification.
        OrderType type = random.nextDouble() < 0.6 ? OrderType.INBOUND : OrderType.OUTBOUND; // 3:2 ratio
        
        Company company = (type == OrderType.INBOUND) ? suppliers.get(random.nextInt(suppliers.size())) : clients.get(random.nextInt(clients.size()));
        
        char randomChar = (char) ('A' + random.nextInt(20));
        int randomNum = random.nextInt(12) + 1;
        String locationCode = String.format("%c-%02d", randomChar, randomNum);

        InOutOrder order = InOutOrder.builder()
                .type(type)
                .status(status)
                .company(company)
                .locationCode(locationCode)
                .expectedDate(date)
                .build();
        
        // Manually set created/updated times for realism
        order.setCreatedAt(date.atTime(random.nextInt(8) + 9, random.nextInt(60)));
        order.setUpdatedAt(order.getCreatedAt());

        int numOrderItems = random.nextInt(3) + 1;
        List<OrderItem> orderItems = new ArrayList<>();
        for (int j = 0; j < numOrderItems; j++) {
            Item randomItem = allItems.get(random.nextInt(allItems.size()));
            int quantity = random.nextInt(141) + 10; // 10 to 150
            
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .item(randomItem)
                    .requestedQuantity(quantity)
                    .processedQuantity(status == OrderStatus.COMPLETED ? quantity : 0)
                    .build();
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        inOutOrderRepository.save(order);
    }
}
