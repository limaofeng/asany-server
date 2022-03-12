package cn.asany.email.rrt.component;

import java.util.HashMap;
import java.util.Map;
import org.apache.james.core.Domain;
import org.apache.james.rrt.api.RecipientRewriteTableException;
import org.apache.james.rrt.lib.*;

public class JPARecipientRewriteTable extends AbstractRecipientRewriteTable {
  @Override
  public void addMapping(MappingSource source, Mapping mapping)
      throws RecipientRewriteTableException {}

  @Override
  public void removeMapping(MappingSource source, Mapping mapping)
      throws RecipientRewriteTableException {}

  @Override
  public Mappings getStoredMappings(MappingSource source) throws RecipientRewriteTableException {
    return null;
  }

  @Override
  public Map<MappingSource, Mappings> getAllMappings() throws RecipientRewriteTableException {
    return new HashMap<>();
  }

  @Override
  protected Mappings mapAddress(String user, Domain domain) throws RecipientRewriteTableException {
    return MappingsImpl.builder().add(user + "@" + domain.asString()).build();
  }
}
