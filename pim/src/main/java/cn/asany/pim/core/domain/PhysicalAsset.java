package cn.asany.pim.core.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PHYSICAL")
public class PhysicalAsset extends Asset {}
