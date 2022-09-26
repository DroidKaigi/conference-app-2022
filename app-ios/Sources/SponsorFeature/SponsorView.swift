import appioscombined
import CommonComponents
import ComposableArchitecture
import Kingfisher
import Model
import SafariView
import SwiftUI
import Theme

public struct SponsorState: Equatable {

    public struct SheetItem: Identifiable, Equatable {
        public var id: UUID = UUID()
        public var sponsor: Sponsor
    }

    public var sponsors: [Sponsor]
    public var isLoading: Bool
    public var sheetItem: SheetItem?

    public init(
        sponsors: [Sponsor] = [],
        isLoading: Bool = false,
        sheetItem: SheetItem? = nil
    ) {
        self.isLoading = isLoading
        self.sponsors = sponsors
        self.sheetItem = sheetItem
    }
}

public enum SponsorAction {
    case refresh
    case refreshResponse(TaskResult<[Sponsor]>)
    case showLinkSheet(Sponsor)
    case hideLinkSheet
}
public struct SponsorEnvironment {
    public let sponsorsRepository: SponsorsRepository

    public init(sponsorsRepository: SponsorsRepository) {
        self.sponsorsRepository = sponsorsRepository
    }
}
public let sponsorReducer = Reducer<SponsorState, SponsorAction, SponsorEnvironment> { state, action, environment in
    switch action {
    case .refresh:
        state.isLoading = true
        return .run { @MainActor subscriber in
            for try await result: [Sponsor] in environment.sponsorsRepository.sponsors().stream() {
                await subscriber.send(
                    .refreshResponse(
                        TaskResult {
                            let prefetcher = ImagePrefetcher(urls: result.compactMap { URL(string: $0.logo) }, options: nil)
                            prefetcher.start()
                            return result
                        }
                    )
                )
            }
        }
        .receive(on: DispatchQueue.main.eraseToAnyScheduler())
        .eraseToEffect()
    case .refreshResponse(.success(let sponsors)):
        state.isLoading = false
        state.sponsors = sponsors
        return .none
    case .refreshResponse(.failure(let error)):
        state.isLoading = false
        return .none
    case .showLinkSheet(let sponsor):
        state.sheetItem = SponsorState.SheetItem(sponsor: sponsor)
        return .none
    case .hideLinkSheet:
        state.sheetItem = nil
        return .none
    }
}

public struct SponsorView: View {
    private let store: Store<SponsorState, SponsorAction>

    public init(store: Store<SponsorState, SponsorAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            ZStack(alignment: .center) {
                if viewStore.isLoading {
                    LoadingView()
                } else {
                    ScrollView {
                        VStack(alignment: .leading, spacing: 24) {
                            SponsorGridView(
                                title: "PLATINUM SPONSORS",
                                sponsors: viewStore.sponsors.filter { $0.plan == .platinum },
                                columns: 1,
                                action: { sponsor in
                                    viewStore.send(.showLinkSheet(sponsor))
                                }
                            )
                            Divider().padding(.horizontal, 16)
                            SponsorGridView(
                                title: "GOLD SPONSORS",
                                sponsors: viewStore.sponsors.filter { $0.plan == .gold },
                                columns: 2,
                                action: { sponsor in
                                    viewStore.send(.showLinkSheet(sponsor))
                                }
                            )
                            Divider().padding(.horizontal, 16)
                            SponsorGridView(
                                title: "SPONSORS",
                                sponsors: viewStore.sponsors.filter { $0.plan == .supporter },
                                columns: 2,
                                action: { sponsor in
                                    viewStore.send(.showLinkSheet(sponsor))
                                }
                            )
                        }
                        .padding(.horizontal, 16)
                        .padding(.vertical, 24)
                    }
                    .sheet(item: viewStore.binding(get: { $0.sheetItem }, send: .hideLinkSheet)) { item in
                        if let url = URL(string: item.sponsor.link) {
                            SafariView(url: url)
                        }
                    }
                }
            }
            .task {
                viewStore.send(.refresh)
            }
            .frame(minWidth: 0, maxWidth: .infinity)
            .frame(minHeight: 0, maxHeight: .infinity)
            .background(AssetColors.background.swiftUIColor)
            .foregroundColor(AssetColors.onBackground.swiftUIColor)
            .navigationTitle(StringsKt.shared.title_sponsors.localized())
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}

struct SponsorGridView: View {

    let title: String
    let sponsors: [Sponsor]
    let columns: Int
    var action: (Sponsor) -> Void

    var body: some View {
        VStack(alignment: .leading) {
            Text(title)
                .font(.system(size: 22, weight: .semibold, design: .default))
            LazyVGrid(columns: Array(repeating: GridItem(spacing: 16), count: columns), spacing: 16) {
                ForEach(sponsors) { sponsor in
                    SponsorItemView(sponsor: sponsor) {
                        action(sponsor)
                    }
                }
            }
        }
    }
}

struct SponsorItemView: View {

    let sponsor: Sponsor
    let action: () -> Void

    var body: some View {
        ZStack {
            AssetColors.white.swiftUIColor
            Button {
                action()
            } label: {
                NetworkImage(url: URL(string: sponsor.logo))
                    .scaledToFit()
                    .frame(minWidth: 0, maxWidth: .infinity)
                    .frame(minHeight: 0, maxHeight: .infinity)
            }
        }
        .frame(height: height(of: sponsor.plan))
        .clipShape(RoundedRectangle(cornerRadius: 8, style: .continuous))
    }

    private func height(of plan: Plan) -> CGFloat {
        switch plan {
        case .platinum:
            return 112
        case .gold:
            return 112
        case .supporter:
            return 72
        default:
            return 72
        }
    }
}

#if DEBUG
struct SponsorView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            SponsorView(
                store: .init(
                    initialState: .init(
                        sponsors: Sponsor.companion.fakes(),
                        isLoading: false
                    ),
                    reducer: .empty,
                    environment: SponsorEnvironment(
                        sponsorsRepository: FakeSponsorsRepository()
                    )
                )
            )
        }
    }
}
struct SponsorView_Loading_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            SponsorView(
                store: .init(
                    initialState: .init(
                        sponsors: [],
                        isLoading: true
                    ),
                    reducer: .empty,
                    environment: SponsorEnvironment(
                        sponsorsRepository: FakeSponsorsRepository()
                    )
                )
            )
        }
    }
}
#endif
