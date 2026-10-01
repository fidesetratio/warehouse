package com.app.warehouse.config;

import com.app.warehouse.entity.Item;
import com.app.warehouse.entity.ItemVariant;
import com.app.warehouse.repository.ItemRepository;
import com.app.warehouse.repository.ItemVariantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            ItemRepository itemRepository,
            ItemVariantRepository variantRepository) {

        return args -> {

            if (itemRepository.count() > 0) {
                return;
            }

            Item tShirt = new Item();
            tShirt.setName("T-Shirt");
            tShirt.setDescription("Cotton T-Shirt");
            itemRepository.save(tShirt);

            ItemVariant redM = new ItemVariant();
            redM.setItem(tShirt);
            redM.setSku("TSHIRT-RED-M");
            redM.setName("Red / M");
            redM.setPrice(new BigDecimal("100000"));
            redM.setStock(20);
            variantRepository.save(redM);

            ItemVariant blueL = new ItemVariant();
            blueL.setItem(tShirt);
            blueL.setSku("TSHIRT-BLUE-L");
            blueL.setName("Blue / L");
            blueL.setPrice(new BigDecimal("110000"));
            blueL.setStock(15);
            variantRepository.save(blueL);


            Item laptop = new Item();
            laptop.setName("Laptop");
            laptop.setDescription("Business Laptop");
            itemRepository.save(laptop);

            ItemVariant laptop8Gb = new ItemVariant();
            laptop8Gb.setItem(laptop);
            laptop8Gb.setSku("LAPTOP-8GB");
            laptop8Gb.setName("8GB RAM");
            laptop8Gb.setPrice(new BigDecimal("7500000"));
            laptop8Gb.setStock(10);
            variantRepository.save(laptop8Gb);

            ItemVariant laptop16Gb = new ItemVariant();
            laptop16Gb.setItem(laptop);
            laptop16Gb.setSku("LAPTOP-16GB");
            laptop16Gb.setName("16GB RAM");
            laptop16Gb.setPrice(new BigDecimal("9000000"));
            laptop16Gb.setStock(5);
            variantRepository.save(laptop16Gb);
        };
    }
}