package by.ilyushenko.tech.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.ChainFileListFilter;
import org.springframework.integration.file.filters.FileSystemPersistentAcceptOnceFileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.messaging.MessageChannel;

import java.io.File;

@Configuration
public class FileReadingMessageConfig {

    @Value("${path.new.files}")
    private String pathNewFiles;

    @Value("${path.processed.files}")
    private String pathProcessedFiles;

    @Bean
    public IntegrationFlow processFileFlow() {
        return IntegrationFlows
                .from("fileInputChannel")
                .handle("fileProcessor", "process").get();
    }

    @Bean
    public MessageChannel fileInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileInputChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() {
        ChainFileListFilter<File> filterCsv = new ChainFileListFilter<>();
        filterCsv.addFilter(new RegexPatternFileListFilter("^.*\\.(csv|json)$"));

        FileSystemPersistentAcceptOnceFileListFilter persistent = new FileSystemPersistentAcceptOnceFileListFilter(store(), "dailyfilesystem");
        persistent.setFlushOnUpdate(true);
        filterCsv.addFilter(persistent);

        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setAutoCreateDirectory(true);
        source.setDirectory(new File(pathNewFiles));
        source.setFilter(filterCsv);
        return source;
    }

    @Bean
    public PropertiesPersistingMetadataStore store() {
        PropertiesPersistingMetadataStore store = new PropertiesPersistingMetadataStore();
        store.setBaseDirectory(pathProcessedFiles);
        return store;
    }
}
