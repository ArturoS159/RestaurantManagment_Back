package com.przemarcz.order.util.payumodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
public class Product {
    private String name;
    private String unitPrice;
    private Integer quantity;
}
