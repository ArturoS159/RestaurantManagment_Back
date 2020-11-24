package com.przemarcz.order.util.payumodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
public class Buyer {
    private String email;
    private String firstName;
    private String lastName;
}
