package cn.asany.pim.core.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PHYSICAL")
public class PhysicalAsset extends Asset {}
